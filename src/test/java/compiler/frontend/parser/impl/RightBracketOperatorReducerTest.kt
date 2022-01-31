package compiler.frontend.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.frontend.parser.impl.internal.IExpressionNodeReducer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class RightBracketOperatorReducerTest {
    private val binaryArrayExpressionNodeReducer = Mockito.mock(IExpressionNodeReducer::class.java)

    private val rightBracketOperatorReducer = RightBracketOperatorReducer(binaryArrayExpressionNodeReducer)

    @Test
    fun shouldBreakTest() {
        val operatorItem = Mockito.mock(OperatorShiftReduceStackItem::class.java)
        val lookAheadValue = "lookAheadValue"
        val parseStack = Stack<IShiftReduceStackItem>()
        val leftRightParentheses = 0
        val leftRightBracket = 0

        val reducerResult = rightBracketOperatorReducer.reduce(
            operatorItem,
            lookAheadValue,
            parseStack,
            leftRightParentheses,
            leftRightBracket
        )

        Assertions.assertEquals(leftRightParentheses, reducerResult.leftRightParentheses)
        Assertions.assertEquals(leftRightBracket - 1, reducerResult.leftRightBracket)
        Assertions.assertTrue(reducerResult.continueReducing)
        Assertions.assertTrue(reducerResult.shouldBreak)
        Assertions.assertTrue(reducerResult.hasNotSeenParentheses)
    }

    @Test
    fun shouldNotBreakTest() {
        val operatorItem = Mockito.mock(OperatorShiftReduceStackItem::class.java)
        val lookAheadValue = "lookAheadValue"
        val parseStack = Stack<IShiftReduceStackItem>()
        val leftRightParentheses = 0
        val leftRightBracket = 1

        val nodeItem = Mockito.mock(NodeShiftReduceStackItem::class.java)
        parseStack.push(nodeItem)

        val node = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(nodeItem.node).thenReturn(node)

        val operator = "operator"
        Mockito.`when`(operatorItem.operator).thenReturn(operator)

        val reducerResult = rightBracketOperatorReducer.reduce(
            operatorItem,
            lookAheadValue,
            parseStack,
            leftRightParentheses,
            leftRightBracket
        )

        Assertions.assertEquals(leftRightParentheses, reducerResult.leftRightParentheses)
        Assertions.assertEquals(leftRightBracket - 1, reducerResult.leftRightBracket)
        Assertions.assertTrue(reducerResult.continueReducing)
        Assertions.assertFalse(reducerResult.shouldBreak)
        Assertions.assertTrue(reducerResult.hasNotSeenParentheses)
        Mockito.verify(binaryArrayExpressionNodeReducer).reduceToExpressionNode(
            node,
            operator,
            parseStack
        )
    }
}