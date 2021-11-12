package compiler.parser

import compiler.parser.impl.DeclarationChildParser
import compiler.parser.impl.DeclarationStatementParser
import compiler.parser.impl.FunctionDeclarationParser
import compiler.parser.impl.Parser
import compiler.parser.impl.internal.IStatementParser
import compiler.parser.impl.internal.ITokenTypeAsserter
import compiler.parser.impl.internal.IVariableDeclarationListParser
import org.mockito.Mockito

enum class ParserSingleton {
    INSTANCE;

    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)

    private val statementParser = Mockito.mock(IStatementParser::class.java)

    private val functionDeclarationParser = FunctionDeclarationParser(
        tokenTypeAsserter,
        statementParser
    )

    private val variableDeclarationParser = Mockito.mock(IVariableDeclarationListParser::class.java)

    private val declarationChildParser = DeclarationChildParser(
        functionDeclarationParser,
        variableDeclarationParser
    )

    private val declarationStatementParser = DeclarationStatementParser(
        tokenTypeAsserter,
        declarationChildParser
    )

    val parser: IParser = Parser(declarationStatementParser)
}