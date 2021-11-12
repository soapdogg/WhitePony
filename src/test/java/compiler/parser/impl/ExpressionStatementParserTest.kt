package compiler.parser.impl

import compiler.core.IExpressionNode
import compiler.core.Token
import compiler.core.TokenType
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.ITokenTypeAsserter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ExpressionStatementParserTest {

    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)
    private val expressionParser = Mockito.mock(IExpressionParser::class.java)

    private val expressionStatementParser = ExpressionStatementParser(
        tokenTypeAsserter,
        expressionParser
    )

    @Test
    fun parseTest() {
        val tokens = listOf<Token>()
        val startingPosition = 0
        val expressionNode = Mockito.mock(IExpressionNode::class.java)
        val positionAfterExpression = startingPosition + 1
        Mockito.`when`(
            expressionParser.parse(
                tokens,
                startingPosition,
                setOf(TokenType.SEMICOLON)
            )
        ).thenReturn(Pair(expressionNode, positionAfterExpression))
        val positionAfterSemicolon = positionAfterExpression + 1
        val (actualExpressionStatementNode, actualFinalPosition) = expressionStatementParser.parse(tokens, startingPosition)
        Assertions.assertEquals(actualExpressionStatementNode.expressionNode, expressionNode)
        Assertions.assertEquals(positionAfterSemicolon, actualFinalPosition)
        Mockito.verify(tokenTypeAsserter).assertTokenType(tokens, positionAfterExpression, TokenType.SEMICOLON)
    }
}