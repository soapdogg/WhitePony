package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.parser.impl.internal.IExpressionNodeReducer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class RightParenthesesOperatorReducerTest {
    private val innerExpressionNodeReducer = Mockito.mock(IExpressionNodeReducer::class.java)
    private val operator = "operator"
    private val operators = setOf(operator)

    private val rightParenthesesOperatorReducer = RightParenthesesOperatorReducer(innerExpressionNodeReducer, operators)

    @Test
    fun shouldBreakTest() {
        val operatorItem = Mockito.mock(OperatorShiftReduceStackItem::class.java)
        val lookAheadValue = "lookAheadValue"
        val parseStack = Stack<IShiftReduceStackItem>()
        val leftRightParentheses = 0
        val leftRightBracket = 0

        val reducerResult = rightParenthesesOperatorReducer.reduce(
            operatorItem,
            lookAheadValue,
            parseStack,
            leftRightParentheses,
            leftRightBracket
        )

        Assertions.assertEquals(leftRightParentheses - 1, reducerResult.leftRightParentheses)
        Assertions.assertEquals(leftRightBracket, reducerResult.leftRightBracket)
        Assertions.assertTrue(reducerResult.continueReducing)
        Assertions.assertTrue(reducerResult.shouldBreak)
        Assertions.assertTrue(reducerResult.hasNotSeenParentheses)
    }

    @Test
    fun shouldNotBreakTest() {
        val operatorItem = Mockito.mock(OperatorShiftReduceStackItem::class.java)
        val lookAheadValue = "lookAheadValue"
        val parseStack = Stack<IShiftReduceStackItem>()
        val leftRightParentheses = 1
        val leftRightBracket = 0

        val nodeItem = Mockito.mock(NodeShiftReduceStackItem::class.java)
        parseStack.push(nodeItem)

        val node = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(nodeItem.node).thenReturn(node)

        Mockito.`when`(operatorItem.operator).thenReturn(operator)

        val reducerResult = rightParenthesesOperatorReducer.reduce(
            operatorItem,
            lookAheadValue,
            parseStack,
            leftRightParentheses,
            leftRightBracket
        )

        Assertions.assertEquals(leftRightParentheses - 1, reducerResult.leftRightParentheses)
        Assertions.assertEquals(leftRightBracket, reducerResult.leftRightBracket)
        Assertions.assertTrue(reducerResult.continueReducing)
        Assertions.assertFalse(reducerResult.shouldBreak)
        Assertions.assertFalse(reducerResult.hasNotSeenParentheses)
        Mockito.verify(innerExpressionNodeReducer).reduceToExpressionNode(
            node,
            operator,
            parseStack
        )
    }
}