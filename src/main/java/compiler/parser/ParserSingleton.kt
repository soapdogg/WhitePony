package compiler.parser

import compiler.core.constants.TokenizerConstants
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.*
import compiler.parser.impl.DeclarationStatementParser
import compiler.parser.impl.FunctionDeclarationParser
import compiler.parser.impl.Parser

enum class ParserSingleton {
    INSTANCE;

    private val tokenTypeAsserter = TokenTypeAsserter()

    private val stackGenerator = StackGenerator()

    private val shifter = Shifter()

    private val operatorPrecedenceMap = mapOf(
        TokenizerConstants.LEFT_BRACKET to 0,
        TokenizerConstants.INCREMENT to 1,
        TokenizerConstants.DECREMENT to 1,
        TokenizerConstants.MULTIPLY_OPERATOR to 2,
        TokenizerConstants.DIVIDE_OPERATOR to 2,
        TokenizerConstants.MODULUS_OPERATOR to 2,
        TokenizerConstants.PLUS_OPERATOR to 3,
        TokenizerConstants.MINUS_OPERATOR to 3,
        TokenizerConstants.LEFT_SHIFT_OPERATOR to 4,
        TokenizerConstants.RIGHT_SHIFT_OPERATOR to 4,
        TokenizerConstants.LESS_THAN_OPERATOR to 5,
        TokenizerConstants.LESS_THAN_EQUALS_OPERATOR to 5,
        TokenizerConstants.GREATER_THAN_OPERATOR to 5,
        TokenizerConstants.GREATER_THAN_EQUALS_OPERATOR to 5,
        TokenizerConstants.RELATIONAL_EQUALS to 6,
        TokenizerConstants.RELATIONAL_NOT_EQUALS to 6,
        TokenizerConstants.BITWISE_AND_OPERATOR to 7,
        TokenizerConstants.BITWISE_XOR_OPERATOR to 8,
        TokenizerConstants.BITWISE_OR_OPERATOR to 9,
        TokenizerConstants.AND_OPERATOR to 10,
        TokenizerConstants.OR_OPERATOR to 11,
        TokenizerConstants.ASSIGN_OPERATOR to 12,
        TokenizerConstants.LEFT_SHIFT_ASSIGN_OPERATOR to 12,
        TokenizerConstants.RIGHT_SHIFT_ASSIGN_OPERATOR to 12,
        TokenizerConstants.AND_ASSIGN_OPERATOR to 12,
        TokenizerConstants.DIVIDE_ASSIGN_OPERATOR to 12,
        TokenizerConstants.MINUS_ASSIGN_OPERATOR to 12,
        TokenizerConstants.MODULUS_ASSIGN_OPERATOR to 12,
        TokenizerConstants.MULTIPLY_ASSIGN_OPERATOR to 12,
        TokenizerConstants.OR_ASSIGN_OPERATOR to 12,
        TokenizerConstants.PLUS_ASSIGN_OPERATOR to 12,
        TokenizerConstants.XOR_ASSIGN_OPERATOR to 12
    )

    private val assignOperatorSet = setOf(
        TokenizerConstants.ASSIGN_OPERATOR,
        TokenizerConstants.LEFT_SHIFT_ASSIGN_OPERATOR,
        TokenizerConstants.RIGHT_SHIFT_ASSIGN_OPERATOR,
        TokenizerConstants.AND_ASSIGN_OPERATOR,
        TokenizerConstants.DIVIDE_ASSIGN_OPERATOR,
        TokenizerConstants.MINUS_ASSIGN_OPERATOR,
        TokenizerConstants.MODULUS_ASSIGN_OPERATOR,
        TokenizerConstants.MULTIPLY_ASSIGN_OPERATOR,
        TokenizerConstants.OR_ASSIGN_OPERATOR,
        TokenizerConstants.PLUS_ASSIGN_OPERATOR,
        TokenizerConstants.XOR_ASSIGN_OPERATOR
    )

    private val operatorPrecedenceDeterminer = OperatorPrecedenceDeterminer(
        operatorPrecedenceMap,
        assignOperatorSet
    )

    private val acceptedTokenTypes = setOf(
        TokenType.FLOATING_POINT,
        TokenType.INTEGER,
        TokenType.IDENTIFIER,
        TokenType.PLUS_MINUS,
        TokenType.PRE_POST,
        TokenType.BINARY_OPERATOR,
        TokenType.BINARY_RELATIONAL_OPERATOR,
        TokenType.BINARY_AND,
        TokenType.BINARY_OR,
        TokenType.BINARY_ASSIGN,
        TokenType.BINARY_ASSIGN_OP,
        TokenType.UNARY_NOT,
        TokenType.BIT_NEGATION,
        TokenType.LEFT_PARENTHESES,
        TokenType.RIGHT_PARENTHESES,
        TokenType.LEFT_BRACKET,
        TokenType.RIGHT_BRACKET
    )

    private val binaryOperatorExpressionNodeReducer = BinaryOperatorExpressionNodeReducer()
    private val binaryRelationalOperatorExpressionNodeReducer = BinaryRelationalOperatorExpressionNodeReducer()
    private val binaryAndExpressionNodeReducer = BinaryAndExpressionNodeReducer()
    private val binaryOrExpressionNodeReducer = BinaryOrExpressionNodeReducer()
    private val binaryAssignExpressionNodeReducer = BinaryAssignExpressionNodeReducer()
    private val binaryAssignOperatorExpressionNodeReducer = BinaryAssignOperatorExpressionNodeReducer()

    private val binaryExpressionNodeReducerMap = mapOf(
        TokenizerConstants.AND_OPERATOR to binaryAndExpressionNodeReducer,
        TokenizerConstants.OR_OPERATOR to binaryOrExpressionNodeReducer,
        TokenizerConstants.ASSIGN_OPERATOR to binaryAssignExpressionNodeReducer,
        TokenizerConstants.LESS_THAN_OPERATOR to binaryRelationalOperatorExpressionNodeReducer,
        TokenizerConstants.LESS_THAN_EQUALS_OPERATOR to binaryRelationalOperatorExpressionNodeReducer,
        TokenizerConstants.GREATER_THAN_OPERATOR to binaryRelationalOperatorExpressionNodeReducer,
        TokenizerConstants.GREATER_THAN_EQUALS_OPERATOR to binaryRelationalOperatorExpressionNodeReducer,
        TokenizerConstants.RELATIONAL_EQUALS to binaryRelationalOperatorExpressionNodeReducer,
        TokenizerConstants.RELATIONAL_NOT_EQUALS to binaryRelationalOperatorExpressionNodeReducer,
        TokenizerConstants.LEFT_SHIFT_ASSIGN_OPERATOR to binaryAssignOperatorExpressionNodeReducer,
        TokenizerConstants.RIGHT_SHIFT_ASSIGN_OPERATOR to binaryAssignOperatorExpressionNodeReducer,
        TokenizerConstants.AND_ASSIGN_OPERATOR to binaryAssignOperatorExpressionNodeReducer,
        TokenizerConstants.DIVIDE_ASSIGN_OPERATOR to binaryAssignOperatorExpressionNodeReducer,
        TokenizerConstants.MINUS_ASSIGN_OPERATOR to binaryAssignOperatorExpressionNodeReducer,
        TokenizerConstants.MODULUS_ASSIGN_OPERATOR to binaryAssignOperatorExpressionNodeReducer,
        TokenizerConstants.MULTIPLY_ASSIGN_OPERATOR to binaryAssignOperatorExpressionNodeReducer,
        TokenizerConstants.OR_ASSIGN_OPERATOR to binaryAssignOperatorExpressionNodeReducer,
        TokenizerConstants.PLUS_ASSIGN_OPERATOR to binaryAssignOperatorExpressionNodeReducer,
        TokenizerConstants.XOR_ASSIGN_OPERATOR to binaryAssignOperatorExpressionNodeReducer,
        TokenizerConstants.DIVIDE_OPERATOR to binaryOperatorExpressionNodeReducer,
        TokenizerConstants.MODULUS_OPERATOR to binaryOperatorExpressionNodeReducer,
        TokenizerConstants.MULTIPLY_OPERATOR to binaryOperatorExpressionNodeReducer,
        TokenizerConstants.LEFT_SHIFT_OPERATOR to binaryOperatorExpressionNodeReducer,
        TokenizerConstants.RIGHT_SHIFT_OPERATOR to binaryOperatorExpressionNodeReducer,
        TokenizerConstants.BITWISE_AND_OPERATOR to binaryOperatorExpressionNodeReducer,
        TokenizerConstants.BITWISE_OR_OPERATOR to binaryOperatorExpressionNodeReducer,
        TokenizerConstants.BITWISE_XOR_OPERATOR to binaryOperatorExpressionNodeReducer,
    )

    private val reductionEnder = ReductionEnder()

    private val shiftReduceExpressionParser = ShiftReduceExpressionParser(
        shifter,
        binaryExpressionNodeReducerMap,
        reductionEnder,
        operatorPrecedenceDeterminer,
        acceptedTokenTypes,
        binaryOperatorExpressionNodeReducer
    )

    private val arrayParser = ArrayParser(
        tokenTypeAsserter,
        shiftReduceExpressionParser
    )
    private val assignParser = AssignParser(
        tokenTypeAsserter,
        shiftReduceExpressionParser
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
        shiftReduceExpressionParser
    )

    private val returnStatementParser = ReturnStatementParser(
        tokenTypeAsserter,
        expressionStatementParser
    )

    private val startDoStatementParser = StartDoStatementParser(
        tokenTypeAsserter
    )

    private val startForStatementParser = StartForStatementParser(
        tokenTypeAsserter,
        shiftReduceExpressionParser
    )

    private val startIfStatementParser = StartIfStatementParser(
        tokenTypeAsserter,
        shiftReduceExpressionParser
    )

    private val startBasicBlockStatementParser = StartBasicBlockStatementParser(
        tokenTypeAsserter
    )

    private val startReturnStatementParser = StartReturnStatementParser(
        returnStatementParser
    )

    private val startVariableDeclarationListStatementParser = StartVariableDeclarationListStatementParser(
        variableDeclarationListParser
    )

    private val startWhileStatementParser = StartWhileStatementParser(
        tokenTypeAsserter,
        shiftReduceExpressionParser
    )

    private val startExpressionStatementParser = StartExpressionStatementParser(
        expressionStatementParser
    )

    private val tokenTypeToParserMap = mapOf(
        TokenType.DO to startDoStatementParser,
        TokenType.FOR to startForStatementParser,
        TokenType.IF to startIfStatementParser,
        TokenType.LEFT_BRACE to startBasicBlockStatementParser,
        TokenType.RETURN to startReturnStatementParser,
        TokenType.TYPE to startVariableDeclarationListStatementParser,
        TokenType.WHILE to startWhileStatementParser,
    )

    private val startLocationStatementParser = StartLocationStatementParser(
        tokenTypeToParserMap,
        startExpressionStatementParser,
    )

    private val endDoStatementParser = EndDoStatementParser(
        tokenTypeAsserter,
        shiftReduceExpressionParser
    )

    private val endForStatementParser = EndForStatementParser()
    private val endIfStatementParser = EndIfStatementParser(tokenTypeAsserter)
    private val endElseStatementParser = EndElseStatementParser()
    private val endWhileStatementParser = EndWhileStatementParser()
    private val endBasicBlockStatementParser = EndBasicBlockStatementParser(tokenTypeAsserter)

    private val locationToParserMap = mapOf(
        StatementParserLocation.LOCATION_START to startLocationStatementParser,
        StatementParserLocation.LOCATION_DO to endDoStatementParser,
        StatementParserLocation.LOCATION_FOR to endForStatementParser,
        StatementParserLocation.LOCATION_IF to endIfStatementParser,
        StatementParserLocation.LOCATION_ELSE to endElseStatementParser,
        StatementParserLocation.LOCATION_WHILE to endWhileStatementParser,
        StatementParserLocation.LOCATION_BASIC_BLOCK to endBasicBlockStatementParser
    )

    private val statementParserOrchestrator = StatementParserOrchestrator(
        stackGenerator,
        locationToParserMap
    )

    private val functionDeclarationParser = FunctionDeclarationParser(
        tokenTypeAsserter,
        statementParserOrchestrator
    )

    private val declarationStatementParser = DeclarationStatementParser(
        functionDeclarationParser,
        variableDeclarationListParser
    )

    val parser: IParser = Parser(declarationStatementParser)
}