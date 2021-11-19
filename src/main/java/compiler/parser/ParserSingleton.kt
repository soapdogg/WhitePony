package compiler.parser

import compiler.core.TokenType
import compiler.core.constants.ParserConstants
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
    private val unaryValues = setOf(TokenizerConstants.PLUS_OPERATOR, TokenizerConstants.MINUS_OPERATOR, TokenizerConstants.INCREMENT, TokenizerConstants.DECREMENT, TokenizerConstants.NEGATION, TokenizerConstants.BIT_NEGATION)
    private val arrayExpressionTokenTypes = setOf(TokenType.LEFT_BRACKET)
    private val arrayExpressionValues = setOf(TokenizerConstants.LEFT_BRACKET)
    private val innerExpressionTokenTypes = setOf(TokenType.LEFT_PARENTHESES)
    private val innerExpressionValues = setOf(TokenizerConstants.LEFT_PARENTHESES)
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

    private val expressionParser = ExpressionParser(
        expressionStackPusher,
        unaryTokenTypes,
        unaryValues,
        arrayExpressionTokenTypes,
        arrayExpressionValues,
        innerExpressionTokenTypes,
        innerExpressionValues,
        binaryOrTokenTypes,
        binaryOrValues,
        binaryAndTokenTypes,
        binaryAndValues,
        binaryOperatorTokenTypes,
        bitwiseOrValues,
        bitwiseXorValues,
        bitwiseAndValues,
        relationalEqualsValues,
        relationalOperatorValues,
        relationalOperatorTokenTypes,
        shiftValues,
        factorValues,
        termTokenTypes,
        termValues,
        binaryAssignTokenTypes,
        binaryAssignValues,
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

    private val statementParserRecursive = StatementParserRecursive(
        tokenTypeAsserter,
        expressionParser,
        variableDeclarationListParser,
        returnStatementParser,
        expressionStatementParser
    )

    private val recursiveFunctionDeclarationParser = FunctionDeclarationParser(
        tokenTypeAsserter,
        statementParserRecursive
    )


    private val recursiveDeclarationStatementParser = DeclarationStatementParser(
        recursiveFunctionDeclarationParser,
        variableDeclarationListParser
    )


    val recursiveParser: IParser = Parser(recursiveDeclarationStatementParser)
}