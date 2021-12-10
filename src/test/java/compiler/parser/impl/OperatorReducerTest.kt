package compiler.parser.impl

import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.ReducerResult
import compiler.core.stack.Stack
import compiler.parser.impl.internal.IOperatorReducer
import compiler.parser.impl.internal.IReductionEnder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class OperatorReducerTest {
    private val operator = "operator"
    private val reducer = Mockito.mock(IOperatorReducer::class.java)
    private val operatorReducers = mapOf(operator to reducer)
    private val reductionEnder = Mockito.mock(IReductionEnder::class.java)

    private val operatorReducer = OperatorReducer(
        operatorReducers,
        reductionEnder
    )

    @Test
    fun containsOperatorKeyTest() {
        val operatorItem = Mockito.mock(OperatorShiftReduceStackItem::class.java)
        val lookAheadValue = "lookAheadValue"
        val parseStack = Stack<IShiftReduceStackItem>()
        val leftRightParentheses = 0
        val leftRightBracket = 0

        Mockito.`when`(operatorItem.operator).thenReturn(operator)

        val expected = Mockito.mock(ReducerResult::class.java)
        Mockito.`when`(reducer.reduce(operatorItem, lookAheadValue, parseStack, leftRightParentheses, leftRightBracket)).thenReturn(expected)

        val actual = operatorReducer.reduce(operatorItem, lookAheadValue, parseStack, leftRightParentheses, leftRightBracket)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun doesNotContainOperatorKeyTest() {
        val operatorItem = Mockito.mock(OperatorShiftReduceStackItem::class.java)
        val lookAheadValue = "lookAheadValue"
        val parseStack = Stack<IShiftReduceStackItem>()
        val leftRightParentheses = 0
        val leftRightBracket = 0

        Mockito.`when`(operatorItem.operator).thenReturn("different")

        val continueReducing = true
        Mockito.`when`(reductionEnder.endReduction(parseStack, listOf(operatorItem))).thenReturn(continueReducing)

        val actual = operatorReducer.reduce(operatorItem, lookAheadValue, parseStack, leftRightParentheses, leftRightBracket)
        Assertions.assertEquals(leftRightParentheses, actual.leftRightParentheses)
        Assertions.assertEquals(leftRightBracket, actual.leftRightBracket)
        Assertions.assertTrue(actual.continueReducing)
        Assertions.assertFalse(actual.shouldBreak)
        Assertions.assertTrue(actual.hasNotSeenParentheses)
    }
}