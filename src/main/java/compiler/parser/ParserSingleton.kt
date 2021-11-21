package compiler.parser

import compiler.core.TokenType
import compiler.core.constants.ExpressionParserConstants
import compiler.core.constants.TokenizerConstants
import compiler.parser.impl.*
import compiler.parser.impl.DeclarationStatementParser
import compiler.parser.impl.FunctionDeclarationParser
import compiler.parser.impl.Parser

enum class ParserSingleton {
    INSTANCE;

    private val tokenTypeAsserter = TokenTypeAsserter()

    private val expressionStackPusher = ExpressionStackPusher(
        tokenTypeAsserter
    )

    private val unaryTokenTypes = setOf(TokenType.PLUS_MINUS, TokenType.PRE_POST, TokenType.BIT_NEGATION, TokenType.UNARY_NOT)
    private val arrayExpressionTokenTypes = setOf(TokenType.LEFT_BRACKET)
    private val innerExpressionTokenTypes = setOf(TokenType.LEFT_PARENTHESES)
    private val binaryOrTokenTypes = setOf(TokenType.BINARY_OR)
    private val binaryOrValues = setOf(TokenizerConstants.OR_OPERATOR)
    private val binaryAndTokenTypes = setOf(TokenType.BINARY_AND)
    private val binaryAndValues = setOf(TokenizerConstants.AND_OPERATOR)
    private val binaryOperatorTokenTypes = setOf(TokenType.BINARY_OPERATOR)
    private val bitwiseOrValues = setOf(TokenizerConstants.BITWISE_OR_OPERATOR)
    private val bitwiseXorValues = setOf(TokenizerConstants.BITWISE_XOR_OPERATOR)
    private val bitwiseAndValues = setOf(TokenizerConstants.BITWISE_AND_OPERATOR)
    private val relationalEqualsValues = setOf(TokenizerConstants.RELATIONAL_EQUALS, TokenizerConstants.RELATIONAL_NOT_EQUALS)
    private val relationalOperatorValues = setOf(TokenizerConstants.GREATER_THAN_OPERATOR, TokenizerConstants.GREATER_THAN_EQUALS_OPERATOR, TokenizerConstants.LESS_THAN_OPERATOR, TokenizerConstants.LESS_THAN_EQUALS_OPERATOR)
    private val relationalOperatorTokenTypes = setOf(TokenType.BINARY_RELATIONAL_OPERATOR)
    private val shiftValues = setOf(TokenizerConstants.LEFT_SHIFT_OPERATOR, TokenizerConstants.RIGHT_SHIFT_OPERATOR)
    private val factorValues = setOf(TokenizerConstants.MULTIPLY_OPERATOR, TokenizerConstants.DIVIDE_OPERATOR, TokenizerConstants.MODULUS_OPERATOR)
    private val termTokenTypes = setOf(TokenType.PLUS_MINUS)
    private val termValues = setOf(TokenizerConstants.PLUS_OPERATOR, TokenizerConstants.MINUS_OPERATOR)
    private val binaryAssignTokenTypes = setOf(TokenType.BINARY_ASSIGN_OP, TokenType.BINARY_ASSIGN)
    private val binaryAssignValues = setOf(TokenizerConstants.AND_ASSIGN_OPERATOR, TokenizerConstants.DIVIDE_ASSIGN_OPERATOR, TokenizerConstants.LEFT_SHIFT_ASSIGN_OPERATOR, TokenizerConstants.MINUS_ASSIGN_OPERATOR, TokenizerConstants.MODULUS_ASSIGN_OPERATOR, TokenizerConstants.MULTIPLY_ASSIGN_OPERATOR, TokenizerConstants.OR_ASSIGN_OPERATOR, TokenizerConstants.PLUS_ASSIGN_OPERATOR, TokenizerConstants.RIGHT_SHIFT_ASSIGN_OPERATOR, TokenizerConstants.XOR_ASSIGN_OPERATOR, TokenizerConstants.ASSIGN_OPERATOR)

    private val locationToAcceptedTokensMap = mapOf(
        ExpressionParserConstants.LOCATION_BINARY_OR to Pair(binaryOrValues, binaryOrTokenTypes),
        ExpressionParserConstants.LOCATION_BINARY_AND to Pair(binaryAndValues, binaryAndTokenTypes),
        ExpressionParserConstants.LOCATION_BITWISE_OR to Pair(bitwiseOrValues, binaryOperatorTokenTypes),
        ExpressionParserConstants.LOCATION_BITWISE_XOR to Pair(bitwiseXorValues, binaryOperatorTokenTypes),
        ExpressionParserConstants.LOCATION_BITWISE_AND to Pair(bitwiseAndValues, binaryOperatorTokenTypes),
        ExpressionParserConstants.LOCATION_RELATIONAL_EQUALS to Pair(relationalEqualsValues, relationalOperatorTokenTypes),
        ExpressionParserConstants.LOCATION_RELATIONAL_OPERATOR to Pair(relationalOperatorValues, relationalOperatorTokenTypes),
        ExpressionParserConstants.LOCATION_SHIFT to Pair(shiftValues, binaryOperatorTokenTypes),
        ExpressionParserConstants.LOCATION_FACTOR to Pair(factorValues, binaryOperatorTokenTypes),
        ExpressionParserConstants.LOCATION_TERM to Pair(termValues, termTokenTypes),
        ExpressionParserConstants.LOCATION_BINARY_ASSIGN to Pair(binaryAssignValues, binaryAssignTokenTypes)
    )

    private val unaryExpressionGenerator = UnaryExpressionGenerator()

    private val binaryOrOperatorExpressionGenerator = BinaryOrOperatorExpressionGenerator()
    private val binaryAndOperatorExpressionGenerator = BinaryAndOperatorExpressionGenerator()
    private val binaryOperatorExpressionGenerator = BinaryOperatorExpressionGenerator()
    private val binaryRelationalOperatorExpressionGenerator = BinaryRelationalOperatorGenerator()
    private val binaryAssignExpressionGenerator = BinaryAssignExpressionGenerator()

    private val binaryExpressionGenerators = mapOf(
        ExpressionParserConstants.LOCATION_BINARY_OR to binaryOrOperatorExpressionGenerator,
        ExpressionParserConstants.LOCATION_BINARY_AND to binaryAndOperatorExpressionGenerator,
        ExpressionParserConstants.LOCATION_BITWISE_OR to binaryOperatorExpressionGenerator,
        ExpressionParserConstants.LOCATION_BITWISE_XOR to binaryOperatorExpressionGenerator,
        ExpressionParserConstants.LOCATION_BITWISE_AND to binaryOperatorExpressionGenerator,
        ExpressionParserConstants.LOCATION_RELATIONAL_EQUALS to binaryRelationalOperatorExpressionGenerator,
        ExpressionParserConstants.LOCATION_RELATIONAL_OPERATOR to binaryRelationalOperatorExpressionGenerator,
        ExpressionParserConstants.LOCATION_SHIFT to binaryOperatorExpressionGenerator,
        ExpressionParserConstants.LOCATION_FACTOR to binaryOperatorExpressionGenerator,
        ExpressionParserConstants.LOCATION_TERM to binaryOperatorExpressionGenerator,
        ExpressionParserConstants.LOCATION_BINARY_ASSIGN to binaryAssignExpressionGenerator
    )

    private val expressionParser = ExpressionParser(
        tokenTypeAsserter,
        expressionStackPusher,
        unaryTokenTypes,
        arrayExpressionTokenTypes,
        innerExpressionTokenTypes,
        locationToAcceptedTokensMap,
        unaryExpressionGenerator,
        binaryExpressionGenerators,
    )
    private val arrayParser = ArrayParser(
        tokenTypeAsserter,
        expressionParser
    )
    private val assignParser = AssignParser(
        tokenTypeAsserter,
        expressionParser,
    )
    private val variableDeclarationParser = VariableDeclarationParser(
        tokenTypeAsserter,
        arrayParser,
        assignParser
    )

    private val variableDeclarationListParser = VariableDeclarationListParser(
        tokenTypeAsserter,
        variableDeclarationParser
    )

    private val expressionStatementParser = ExpressionStatementParser(
        tokenTypeAsserter,
        expressionParser
    )

    private val returnStatementParser = ReturnStatementParser(
        tokenTypeAsserter,
        expressionStatementParser
    )

    private val statementParser = StatementParser(
        tokenTypeAsserter,
        expressionParser,
        variableDeclarationListParser,
        returnStatementParser,
        expressionStatementParser
    )

    private val functionDeclarationParser = FunctionDeclarationParser(
        tokenTypeAsserter,
        statementParser
    )

    private val declarationStatementParser = DeclarationStatementParser(
        functionDeclarationParser,
        variableDeclarationListParser
    )

    val parser: IParser = Parser(declarationStatementParser)
}