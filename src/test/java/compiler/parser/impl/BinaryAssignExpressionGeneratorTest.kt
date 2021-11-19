package compiler.parser.impl

import compiler.core.*
import compiler.core.constants.TokenizerConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class BinaryAssignExpressionGeneratorTest {

    private val binaryAssignExpressionGenerator = BinaryAssignExpressionGenerator()

    @Test
    fun generateBinaryAssignExpressionTest() {
        val leftExpression = Mockito.mock(IParsedExpressionNode::class.java)
        val rightExpression = Mockito.mock(IParsedExpressionNode::class.java)

        val stack = Stack<IParsedExpressionNode>()
        stack.push(leftExpression)
        stack.push(rightExpression)

        val topToken = Mockito.mock(Token::class.java)
        Mockito.`when`(topToken.type).thenReturn(TokenType.BINARY_ASSIGN)

        binaryAssignExpressionGenerator.generateExpression(stack, topToken)

        val top = stack.pop()
        val resultNode = top as ParsedBinaryAssignNode
        Assertions.assertEquals(resultNode.leftExpression, leftExpression)
        Assertions.assertEquals(resultNode.rightExpression, rightExpression)
    }

    @Test
    fun generateBinaryAssignOperatorExpressionTest() {
        val leftExpression = Mockito.mock(IParsedExpressionNode::class.java)
        val rightExpression = Mockito.mock(IParsedExpressionNode::class.java)

        val stack = Stack<IParsedExpressionNode>()
        stack.push(leftExpression)
        stack.push(rightExpression)

        val topToken = Mockito.mock(Token::class.java)
        Mockito.`when`(topToken.type).thenReturn(TokenType.BINARY_ASSIGN_OP)
        Mockito.`when`(topToken.value).thenReturn(TokenizerConstants.AND_ASSIGN_OPERATOR)

        binaryAssignExpressionGenerator.generateExpression(stack, topToken)

        val top = stack.pop()
        val resultNode = top as ParsedBinaryAssignOperatorNode
        Assertions.assertEquals(resultNode.leftExpression, leftExpression)
        Assertions.assertEquals(resultNode.rightExpression, rightExpression)
        Assertions.assertEquals(resultNode.operator, TokenizerConstants.BITWISE_AND_OPERATOR)
    }
}