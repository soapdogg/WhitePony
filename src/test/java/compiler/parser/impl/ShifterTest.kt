package compiler.parser.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.parsed.ParsedConstantExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ShifterTest {
    private val shifter = Shifter()

    @Test
    fun shiftIntConstantNodeTest() {
        val token = Mockito.mock(Token::class.java)
        val tokens = listOf(token)
        val currentPosition = 0
        val parseStack = Stack<IShiftReduceStackItem>()

        val tokenType = TokenType.INTEGER
        Mockito.`when`(token.type).thenReturn(tokenType)

        val value = "value"
        Mockito.`when`(token.value).thenReturn(value)

        val actual = shifter.shift(tokens, currentPosition, parseStack)
        Assertions.assertEquals(currentPosition + 1, actual)

        val top = parseStack.pop() as NodeShiftReduceStackItem
        val node = top.node as ParsedConstantExpressionNode
        Assertions.assertEquals(PrinterConstants.INT, node.type)
        Assertions.assertEquals(value, node.value)
    }

    @Test
    fun shiftDoubleConstantNodeTest() {
        val token = Mockito.mock(Token::class.java)
        val tokens = listOf(token)
        val currentPosition = 0
        val parseStack = Stack<IShiftReduceStackItem>()

        val tokenType = TokenType.FLOATING_POINT
        Mockito.`when`(token.type).thenReturn(tokenType)

        val value = "value"
        Mockito.`when`(token.value).thenReturn(value)

        val actual = shifter.shift(tokens, currentPosition, parseStack)
        Assertions.assertEquals(currentPosition + 1, actual)

        val top = parseStack.pop() as NodeShiftReduceStackItem
        val node = top.node as ParsedConstantExpressionNode
        Assertions.assertEquals(PrinterConstants.DOUBLE, node.type)
        Assertions.assertEquals(value, node.value)
    }

    @Test
    fun shiftVariableNodeTest() {
        val token = Mockito.mock(Token::class.java)
        val tokens = listOf(token)
        val currentPosition = 0
        val parseStack = Stack<IShiftReduceStackItem>()

        val tokenType = TokenType.IDENTIFIER
        Mockito.`when`(token.type).thenReturn(tokenType)

        val value = "value"
        Mockito.`when`(token.value).thenReturn(value)

        val actual = shifter.shift(tokens, currentPosition, parseStack)
        Assertions.assertEquals(currentPosition + 1, actual)

        val top = parseStack.pop() as NodeShiftReduceStackItem
        val node = top.node as ParsedVariableExpressionNode
        Assertions.assertEquals(value, node.value)
    }

    @Test
    fun shiftOperatorNodeTest() {
        val token = Mockito.mock(Token::class.java)
        val tokens = listOf(token)
        val currentPosition = 0
        val parseStack = Stack<IShiftReduceStackItem>()

        val tokenType = TokenType.PLUS_MINUS
        Mockito.`when`(token.type).thenReturn(tokenType)

        val value = "value"
        Mockito.`when`(token.value).thenReturn(value)

        val actual = shifter.shift(tokens, currentPosition, parseStack)
        Assertions.assertEquals(currentPosition + 1, actual)

        val top = parseStack.pop() as OperatorShiftReduceStackItem
        Assertions.assertEquals(value, top.operator)
    }
}