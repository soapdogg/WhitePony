package compiler.parser.impl

import compiler.core.IParsedExpressionNode
import compiler.core.ParsedBinaryOrOperatorNode
import compiler.core.Stack
import compiler.core.Token
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class BinaryOrOperatorExpressionGeneratorTest {

    private val binaryOrOperatorExpressionGenerator = BinaryOrOperatorExpressionGenerator()

    @Test
    fun generateExpressionTest() {
        val leftExpression = Mockito.mock(IParsedExpressionNode::class.java)
        val rightExpression = Mockito.mock(IParsedExpressionNode::class.java)

        val stack = Stack<IParsedExpressionNode>()
        stack.push(leftExpression)
        stack.push(rightExpression)

        val topToken = Mockito.mock(Token::class.java)

        binaryOrOperatorExpressionGenerator.generateExpression(stack, topToken)

        val top = stack.pop()
        val resultNode = top as ParsedBinaryOrOperatorNode
        Assertions.assertEquals(resultNode.leftExpression, leftExpression)
        Assertions.assertEquals(resultNode.rightExpression, rightExpression)
    }
}