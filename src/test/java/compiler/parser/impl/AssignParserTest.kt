package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.ITokenTypeAsserter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class AssignParserTest {

    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)
    private val recursiveExpressionParser = Mockito.mock(IExpressionParser::class.java)
    private val shiftReduceExpressionParser = Mockito.mock(IExpressionParser::class.java)

    private val assignParser = AssignParser(
        tokenTypeAsserter,
        shiftReduceExpressionParser
    )

    @Test
    fun parseTest() {
        val tokens = listOf<Token>()
        val startingPosition = 0

        val positionAfterAssign = startingPosition + 1
        val expressionNode = Mockito.mock(IParsedExpressionNode::class.java)
        val positionAfterExpression = positionAfterAssign + 1
        Mockito.`when`(
            recursiveExpressionParser.parse(
                tokens,
                positionAfterAssign,
            )
        ).thenReturn(Pair(expressionNode, positionAfterExpression))

        val (actualAssignNode, actualFinalPosition) = assignParser.parse(
            tokens,
            startingPosition
        )
        Assertions.assertEquals(expressionNode, actualAssignNode.expressionNode)
        Assertions.assertEquals(positionAfterExpression, actualFinalPosition)
        Mockito.verify(tokenTypeAsserter).assertTokenType(tokens, startingPosition, TokenType.BINARY_ASSIGN)
    }
}