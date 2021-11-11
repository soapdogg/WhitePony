package compiler.parser

import compiler.parser.impl.DeclarationStatementParser
import compiler.parser.impl.Parser
import compiler.parser.impl.internal.IDeclarationChildParser
import compiler.parser.impl.internal.ITokenTypeAsserter
import org.mockito.Mockito

enum class ParserSingleton {
    INSTANCE;

    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)
    private val declarationChildParser = Mockito.mock(IDeclarationChildParser::class.java)

    private val declarationStatementParser = DeclarationStatementParser(
        tokenTypeAsserter,
        declarationChildParser
    )

    val parser: IParser = Parser(declarationStatementParser)
}