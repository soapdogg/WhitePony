package compiler.parser

import compiler.parser.impl.DeclarationStatementParser
import compiler.parser.impl.FunctionDeclarationParser
import compiler.parser.impl.Parser
import compiler.parser.impl.VariableDeclarationListParser
import compiler.parser.impl.internal.IStatementParser
import compiler.parser.impl.internal.ITokenTypeAsserter
import org.mockito.Mockito

enum class ParserSingleton {
    INSTANCE;

    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)

    private val statementParser = Mockito.mock(IStatementParser::class.java)

    private val functionDeclarationParser = FunctionDeclarationParser(
        tokenTypeAsserter,
        statementParser
    )

    private val variableDeclarationListParser = VariableDeclarationListParser(
        tokenTypeAsserter
    )

    private val declarationStatementParser = DeclarationStatementParser(
        functionDeclarationParser,
        variableDeclarationListParser
    )

    val parser: IParser = Parser(declarationStatementParser)
}