package compiler.parser.impl

import compiler.core.*
import compiler.core.constants.TokenizerConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class BinaryRelationalOperatorGeneratorTest {
    private val binaryRelationalOperatorExpressionGenerator = BinaryRelationalOperatorGenerator()

    @Test
    fun generateExpressionTest() {
        val leftExpression = Mockito.mock(IParsedExpressionNode::class.java)
        val rightExpression = Mockito.mock(IParsedExpressionNode::class.java)

        val stack = Stack<IParsedExpressionNode>()
        stack.push(leftExpression)
        stack.push(rightExpression)

        val topToken = Mockito.mock(Token::class.java)
        Mockito.`when`(topToken.value).thenReturn(TokenizerConstants.MULTIPLY_OPERATOR)

        binaryRelationalOperatorExpressionGenerator.generateExpression(stack, topToken)

        val top = stack.pop()
        val resultNode = top as ParsedBinaryRelationalOperatorNode
        Assertions.assertEquals(resultNode.leftExpression, leftExpression)
        Assertions.assertEquals(resultNode.rightExpression, rightExpression)
        Assertions.assertEquals(resultNode.operator, TokenizerConstants.MULTIPLY_OPERATOR)
    }
}