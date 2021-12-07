package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.ITokenTypeAsserter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ArrayParserTest {

    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)
    private val shiftReduceExpressionParser = Mockito.mock(IExpressionParser::class.java)

    private val arrayParser = ArrayParser(
        tokenTypeAsserter,
        shiftReduceExpressionParser
    )

    @Test
    fun hasExpressionTest() {
        val leftBracketToken = Mockito.mock(Token::class.java)

        val indexToken = Mockito.mock(Token::class.java)
        Mockito.`when`(indexToken.type).thenReturn(TokenType.INTEGER)

        val tokens = listOf(leftBracketToken,indexToken)
        val startingPosition = 0

        val expressionPosition = startingPosition + 1
        val expressionNode = Mockito.mock(IParsedExpressionNode::class.java)
        val rightBracketPosition = startingPosition + 2
        Mockito.`when`(
            shiftReduceExpressionParser.parse(
                tokens,
                expressionPosition,
            )
        ).thenReturn(Pair(expressionNode, rightBracketPosition))
        val finalPosition = rightBracketPosition + 1

        val (actualArrayNode, actualFinalPosition) = arrayParser.parse(tokens, startingPosition)
        Assertions.assertEquals(expressionNode, actualArrayNode.indexExpressionNode)
        Assertions.assertEquals(finalPosition, actualFinalPosition)
        Mockito.verify(tokenTypeAsserter).assertTokenType(tokens, startingPosition, TokenType.LEFT_BRACKET)
        Mockito.verify(tokenTypeAsserter).assertTokenType(tokens, rightBracketPosition, TokenType.RIGHT_BRACKET)
    }

    @Test
    fun doesNotHaveExpressionTest() {
        val leftBracketToken = Mockito.mock(Token::class.java)

        val rightBracketToken = Mockito.mock(Token::class.java)
        Mockito.`when`(rightBracketToken.type).thenReturn(TokenType.RIGHT_BRACKET)

        val tokens = listOf(leftBracketToken,rightBracketToken)
        val startingPosition = 0

        val rightBracketPosition = startingPosition + 1

        val finalPosition = rightBracketPosition + 1

        val (actualArrayNode, actualFinalPosition) = arrayParser.parse(tokens, startingPosition)
        Assertions.assertNull(actualArrayNode.indexExpressionNode)
        Assertions.assertEquals(finalPosition, actualFinalPosition)
        Mockito.verify(tokenTypeAsserter).assertTokenType(tokens, startingPosition, TokenType.LEFT_BRACKET)
        Mockito.verify(tokenTypeAsserter).assertTokenType(tokens, rightBracketPosition, TokenType.RIGHT_BRACKET)
    }
}