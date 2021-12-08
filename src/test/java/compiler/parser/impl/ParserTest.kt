package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedDeclarationStatementNode
import compiler.core.tokenizer.Token
import compiler.parser.impl.internal.IDeclarationStatementParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ParserTest {
    private val declarationStatementParser = Mockito.mock(IDeclarationStatementParser::class.java)

    private val parser = Parser(declarationStatementParser)

    @Test
    fun parseTest() {
        val token = Mockito.mock(Token::class.java)
        val tokens = listOf(token)

        val declarationStatementNode = Mockito.mock(IParsedDeclarationStatementNode::class.java)
        Mockito.`when`(declarationStatementParser.parse(
            tokens,
            0,
            false
        )).thenReturn(Pair(declarationStatementNode, tokens.size))

        val actual = parser.parse(
            tokens,
            false
        )
        Assertions.assertEquals(declarationStatementNode, actual.declarationStatements[0])
    }
}