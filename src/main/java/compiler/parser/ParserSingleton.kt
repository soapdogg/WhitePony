package compiler.parser

import compiler.core.stack.StatementParserLocations
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.*
import compiler.parser.impl.DeclarationStatementParser
import compiler.parser.impl.FunctionDeclarationParser
import compiler.parser.impl.Parser

enum class ParserSingleton {
    INSTANCE;

    private val tokenTypeAsserter = TokenTypeAsserter()

    private val expressionParser = ExpressionParser()
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

    private val startDoStatementParser = StartDoStatementParser(
        tokenTypeAsserter
    )

    private val startForStatementParser = StartForStatementParser(
        tokenTypeAsserter,
        expressionParser
    )

    private val startIfStatementParser = StartIfStatementParser(
        tokenTypeAsserter,
        expressionParser
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
        expressionParser
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
        expressionParser
    )

    private val endForStatementParser = EndForStatementParser()
    private val endIfStatementParser = EndIfStatementParser(tokenTypeAsserter)
    private val endElseStatementParser = EndElseStatementParser()
    private val endWhileStatementParser = EndWhileStatementParser()
    private val endBasicBlockStatementParser = EndBasicBlockStatementParser(tokenTypeAsserter)

    private val locationToParserMap = mapOf(
        StatementParserLocations.LOCATION_START to startLocationStatementParser,
        StatementParserLocations.LOCATION_DO to endDoStatementParser,
        StatementParserLocations.LOCATION_FOR to endForStatementParser,
        StatementParserLocations.LOCATION_IF to endIfStatementParser,
        StatementParserLocations.LOCATION_ELSE to endElseStatementParser,
        StatementParserLocations.LOCATION_WHILE to endWhileStatementParser,
        StatementParserLocations.LOCATION_BASIC_BLOCK to endBasicBlockStatementParser
    )

    private val statementParserOrchestrator = StatementParserOrchestrator(
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