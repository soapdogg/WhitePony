package compiler.frontend.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.frontend.parser.impl.internal.IExpressionParser
import compiler.frontend.parser.impl.internal.ITokenTypeAsserter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ExpressionStatementParserTest {

    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)
    private val shiftReduceExpressionParser = Mockito.mock(IExpressionParser::class.java)

    private val expressionStatementParser = ExpressionStatementParser(
        tokenTypeAsserter,
        shiftReduceExpressionParser
    )

    @Test
    fun parseTest() {
        val tokens = listOf<Token>()
        val startingPosition = 0
        val expressionNode = Mockito.mock(IParsedExpressionNode::class.java)
        val positionAfterExpression = startingPosition + 1
        Mockito.`when`(
            shiftReduceExpressionParser.parse(
                tokens,
                startingPosition,
            )
        ).thenReturn(Pair(expressionNode, positionAfterExpression))
        val positionAfterSemicolon = positionAfterExpression + 1
        val (actualExpressionStatementNode, actualFinalPosition) = expressionStatementParser.parse(
            tokens,
            startingPosition
        )
        Assertions.assertEquals(actualExpressionStatementNode.expressionNode, expressionNode)
        Assertions.assertEquals(positionAfterSemicolon, actualFinalPosition)
        Mockito.verify(tokenTypeAsserter).assertTokenType(tokens, positionAfterExpression, TokenType.SEMICOLON)
    }
}