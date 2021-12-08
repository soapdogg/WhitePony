package compiler.parser

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

    private val recursiveExpressionParser = RecursiveExpressionParser()

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
        TokenType.UNARY_NOT,
        TokenType.BIT_NEGATION,
        TokenType.LEFT_PARENTHESES,
        TokenType.RIGHT_PARENTHESES,
        TokenType.LEFT_BRACKET,
        TokenType.RIGHT_BRACKET
    )
    private val shiftReduceExpressionParser = ShiftReduceExpressionParser(
        acceptedTokenTypes
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
        recursiveExpressionParser,
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