package compiler.frontend.parser.impl

import compiler.core.stack.*
import compiler.frontend.parser.impl.internal.INodeReducer
import compiler.frontend.parser.impl.internal.IOperatorReducer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ReducerTest {
    private val nodeReducer = Mockito.mock(INodeReducer::class.java)
    private val operatorReducer = Mockito.mock(IOperatorReducer::class.java)

    private val reducer = Reducer(nodeReducer, operatorReducer)

    @Test
    fun topIsNodeTest() {
        val lookAheadValue = "lookaheadValue"
        val parseStack = Stack<IShiftReduceStackItem>()
        val leftRightParentheses = 0
        val leftRightBracket = 0
        val hasNotSeenParentheses = true

        val top = Mockito.mock(NodeShiftReduceStackItem::class.java)
        parseStack.push(top)

        Mockito.`when`(nodeReducer.reduce(lookAheadValue, top, parseStack)).thenReturn(false)

        val actual = reducer.reduce(lookAheadValue, parseStack, leftRightParentheses, leftRightBracket, hasNotSeenParentheses)
        Assertions.assertEquals(leftRightParentheses, actual.leftRightParentheses)
        Assertions.assertEquals(leftRightBracket, actual.leftRightBracket)
        Assertions.assertFalse(actual.continueReducing)
        Assertions.assertFalse(actual.shouldBreak)
        Assertions.assertEquals(hasNotSeenParentheses, actual.hasNotSeenParentheses)
    }

    @Test
    fun topIsOperatorTest() {
        val lookAheadValue = "lookaheadValue"
        val parseStack = Stack<IShiftReduceStackItem>()
        val leftRightParentheses = 0
        val leftRightBracket = 0
        val hasNotSeenParentheses = true

        val top = Mockito.mock(OperatorShiftReduceStackItem::class.java)
        parseStack.push(top)
        val lrp = 4
        val lrb = 5
        val shouldBreak = true
        val continueReducing = false
        val notSeenParentheses = true
        val expected = ReducerResult(
            lrp,
            lrb,
            continueReducing,
            shouldBreak,
            notSeenParentheses
        )
        Mockito.`when`(operatorReducer.reduce(top, lookAheadValue, parseStack, leftRightParentheses, leftRightBracket)).thenReturn(expected)

        val actual = reducer.reduce(lookAheadValue, parseStack, leftRightParentheses, leftRightBracket, hasNotSeenParentheses)
        Assertions.assertEquals(lrp, actual.leftRightParentheses)
        Assertions.assertEquals(lrb, actual.leftRightBracket)
        Assertions.assertFalse(actual.continueReducing)
        Assertions.assertTrue(actual.shouldBreak)
        Assertions.assertTrue(actual.hasNotSeenParentheses)
    }
}