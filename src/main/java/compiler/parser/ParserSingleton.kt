package compiler.parser

import compiler.parser.impl.*
import compiler.parser.impl.DeclarationStatementParser
import compiler.parser.impl.FunctionDeclarationParser
import compiler.parser.impl.Parser

enum class ParserSingleton {
    INSTANCE;

    private val tokenTypeAsserter = TokenTypeAsserter()

    private val expressionParser = FakeExpressionParser()
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

    private val statementParserIterative = StatementParserIterative(
        tokenTypeAsserter,
        expressionParser,
        returnStatementParser,
        variableDeclarationListParser,
        expressionStatementParser
    )

    private val statementParserRecursive = StatementParserRecursive(
        tokenTypeAsserter,
        expressionParser,
        variableDeclarationListParser,
        returnStatementParser,
        expressionStatementParser
    )

    private val iterativeFunctionDeclarationParser = FunctionDeclarationParser(
        tokenTypeAsserter,
        statementParserIterative
    )

    private val recursiveFunctionDeclarationParser = FunctionDeclarationParser(
        tokenTypeAsserter,
        statementParserRecursive
    )



    private val iterativeDeclarationStatementParser = DeclarationStatementParser(
        iterativeFunctionDeclarationParser,
        variableDeclarationListParser
    )

    private val recursiveDeclarationStatementParser = DeclarationStatementParser(
        recursiveFunctionDeclarationParser,
        variableDeclarationListParser
    )


    val iterativeParser: IParser = Parser(iterativeDeclarationStatementParser)
    val recursiveParser: IParser = Parser(recursiveDeclarationStatementParser)
}