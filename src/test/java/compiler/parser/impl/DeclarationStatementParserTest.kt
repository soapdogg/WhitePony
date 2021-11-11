package compiler.parser.impl

import compiler.core.IDeclarationChildNode
import compiler.core.Token
import compiler.core.TokenType
import compiler.parser.impl.internal.IDeclarationChildParser
import compiler.parser.impl.internal.ITokenTypeAsserter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class DeclarationStatementParserTest {
    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)
    private val declarationChildParser = Mockito.mock(IDeclarationChildParser::class.java)

    private val declarationStatementParser = DeclarationStatementParser(
        tokenTypeAsserter,
        declarationChildParser
    )

    @Test
    fun parseTest() {
        val tokens = listOf<Token>()
        val startingPosition = 0

        val typeToken = Mockito.mock(Token::class.java)
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.TYPE)).thenReturn(typeToken)

        val identifierToken = Mockito.mock(Token::class.java)
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, startingPosition + 1, TokenType.IDENTIFIER)).thenReturn(identifierToken)

        val typeValue = "typeValue"
        Mockito.`when`(typeToken.value).thenReturn(typeValue)

        val identifierValue = "identifierValue"
        Mockito.`when`(identifierToken.value).thenReturn(identifierValue)

        val declarationStatementChild = Mockito.mock(IDeclarationChildNode::class.java)
        val currentPosition = 45
        Mockito.`when`(
            declarationChildParser.parse(
                tokens,
                startingPosition + 2,
                typeValue,
                identifierValue
            )
        ).thenReturn(Pair(declarationStatementChild, currentPosition))

        val (actualDeclarationStatement, actualPosition) = declarationStatementParser.parse(tokens, startingPosition)

        Assertions.assertEquals(declarationStatementChild, actualDeclarationStatement.declarationChild)
        Assertions.assertEquals(currentPosition, actualPosition)
    }
}