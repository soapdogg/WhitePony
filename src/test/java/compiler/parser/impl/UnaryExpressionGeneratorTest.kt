package compiler.parser.impl

import compiler.core.*
import compiler.core.constants.TokenizerConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class UnaryExpressionGeneratorTest {
    private val unaryExpressionGenerator = UnaryExpressionGenerator()

    @Test
    fun generateUnaryOperatorExpressionTest() {
        val insideExpression = Mockito.mock(IParsedExpressionNode::class.java)

        val stack = Stack<IParsedExpressionNode>()
        stack.push(insideExpression)

        val topToken = Mockito.mock(Token::class.java)
        Mockito.`when`(topToken.type).thenReturn(TokenType.PLUS_MINUS)
        Mockito.`when`(topToken.value).thenReturn(TokenizerConstants.MULTIPLY_OPERATOR)

        unaryExpressionGenerator.generateExpression(stack, topToken)

        val top = stack.pop()
        val resultNode = top as ParsedUnaryOperatorNode
        Assertions.assertEquals(resultNode.expression, insideExpression)
        Assertions.assertEquals(resultNode.operator, TokenizerConstants.MULTIPLY_OPERATOR)
    }

    @Test
    fun generateUnaryNotOperatorExpressionTest() {
        val insideExpression = Mockito.mock(IParsedExpressionNode::class.java)

        val stack = Stack<IParsedExpressionNode>()
        stack.push(insideExpression)

        val topToken = Mockito.mock(Token::class.java)
        Mockito.`when`(topToken.type).thenReturn(TokenType.UNARY_NOT)

        unaryExpressionGenerator.generateExpression(stack, topToken)

        val top = stack.pop()
        val resultNode = top as ParsedUnaryNotOperatorNode
        Assertions.assertEquals(resultNode.expression, insideExpression)
    }

    @Test
    fun generateUnaryPreOperatorExpressionTest() {
        val insideExpression = Mockito.mock(IParsedExpressionNode::class.java)

        val stack = Stack<IParsedExpressionNode>()
        stack.push(insideExpression)

        val topToken = Mockito.mock(Token::class.java)
        Mockito.`when`(topToken.type).thenReturn(TokenType.PRE_POST)
        Mockito.`when`(topToken.value).thenReturn(TokenizerConstants.MULTIPLY_OPERATOR)

        unaryExpressionGenerator.generateExpression(stack, topToken)

        val top = stack.pop()
        val resultNode = top as ParsedUnaryPreOperatorNode
        Assertions.assertEquals(resultNode.expression, insideExpression)
        Assertions.assertEquals(resultNode.operator, TokenizerConstants.MULTIPLY_OPERATOR)
    }
}