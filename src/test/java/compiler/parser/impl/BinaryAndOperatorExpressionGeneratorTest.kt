package compiler.parser.impl

import compiler.core.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class BinaryAndOperatorExpressionGeneratorTest {

    private val binaryAndOperatorExpressionGenerator = BinaryAndOperatorExpressionGenerator()

    @Test
    fun generateExpressionTest() {
        val leftExpression = Mockito.mock(IParsedExpressionNode::class.java)
        val rightExpression = Mockito.mock(IParsedExpressionNode::class.java)

        val stack = Stack<IParsedExpressionNode>()
        stack.push(leftExpression)
        stack.push(rightExpression)

        val topToken = Mockito.mock(Token::class.java)

        binaryAndOperatorExpressionGenerator.generateExpression(stack, topToken)

        val top = stack.pop()
        val resultNode = top as ParsedBinaryAndOperatorNode
        Assertions.assertEquals(resultNode.leftExpression, leftExpression)
        Assertions.assertEquals(resultNode.rightExpression, rightExpression)
    }
}