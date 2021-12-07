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
    private val shiftReduceExpressionParser = ShiftReduceExpressionParser()

    private val arrayParser = ArrayParser(
        tokenTypeAsserter,
        recursiveExpressionParser,
        shiftReduceExpressionParser
    )
    private val assignParser = AssignParser(
        tokenTypeAsserter,
        recursiveExpressionParser,
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
        recursiveExpressionParser,
        shiftReduceExpressionParser
    )

    private val startIfStatementParser = StartIfStatementParser(
        tokenTypeAsserter,
        recursiveExpressionParser,
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
        recursiveExpressionParser,
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
        recursiveExpressionParser,
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