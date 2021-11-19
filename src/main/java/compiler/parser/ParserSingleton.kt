package compiler.parser

import compiler.parser.impl.*
import compiler.parser.impl.DeclarationStatementParser
import compiler.parser.impl.FunctionDeclarationParser
import compiler.parser.impl.Parser

enum class ParserSingleton {
    INSTANCE;

    private val tokenTypeAsserter = TokenTypeAsserter()

    private val expressionParserIterative = ExpressionParserIterative()
    private val arrayParser = ArrayParser(
        tokenTypeAsserter,
        expressionParserIterative
    )
    private val assignParser = AssignParser(
        tokenTypeAsserter,
        expressionParserIterative,
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
        expressionParserIterative
    )

    private val returnStatementParser = ReturnStatementParser(
        tokenTypeAsserter,
        expressionStatementParser
    )

    private val statementParserRecursive = StatementParserRecursive(
        tokenTypeAsserter,
        expressionParserIterative,
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