package compiler.parser.impl

import compiler.core.BasicBlockNode
import compiler.core.Token
import compiler.core.TokenType
import compiler.parser.impl.internal.IStatementParser
import compiler.parser.impl.internal.ITokenTypeAsserter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class FunctionDeclarationParserTest {
    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)
    private val statementParser = Mockito.mock(IStatementParser::class.java)

    private val functionDeclarationParser = FunctionDeclarationParser(
        tokenTypeAsserter,
        statementParser
    )

    @Test
    fun parseTest() {
        val tokens = listOf<Token>()
        val startingPosition = 0

        val typeToken = Mockito.mock(Token::class.java)
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.TYPE)).thenReturn(typeToken)

        val type = "type"
        Mockito.`when`(typeToken.value).thenReturn(type)

        val identifierToken = Mockito.mock(Token::class.java)
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, startingPosition + 1, TokenType.IDENTIFIER)).thenReturn(identifierToken)

        val identifierValue = "value"
        Mockito.`when`(identifierToken.value).thenReturn(identifierValue)

        val basicBlockNode = Mockito.mock(BasicBlockNode::class.java)
        val currentPosition = 3
        Mockito.`when`(
            statementParser.parse(
                tokens,
                startingPosition + 4
            )
        ).thenReturn(Pair(basicBlockNode, currentPosition))

        val (actualFunctionNode, actualCurrentPosition) = functionDeclarationParser.parse(
            tokens,
            startingPosition,
        )

        Assertions.assertEquals(identifierValue, actualFunctionNode.functionName)
        Assertions.assertEquals(type, actualFunctionNode.type)
        Assertions.assertEquals(basicBlockNode, actualFunctionNode.basicBlockNode)
        Assertions.assertEquals(currentPosition, actualCurrentPosition)
        Mockito.verify(tokenTypeAsserter).assertTokenType(tokens, startingPosition + 2, TokenType.LEFT_PARENTHESES)
        Mockito.verify(tokenTypeAsserter).assertTokenType(tokens, startingPosition + 3, TokenType.RIGHT_PARENTHESES)
        Mockito.verify(tokenTypeAsserter).assertTokenType(tokens, startingPosition + 4, TokenType.LEFT_BRACE)
    }
}