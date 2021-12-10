package compiler.parser.impl

import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.parser.impl.internal.IReductionEnder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class LeftParenthesesOperatorReducerTest {
    private val reductionEnder = Mockito.mock(IReductionEnder::class.java)

    private val leftParenthesesOperatorReducer = LeftParenthesesOperatorReducer(reductionEnder)

    @Test
    fun reduceTest() {
        val operatorItem = Mockito.mock(OperatorShiftReduceStackItem::class.java)
        val lookAheadValue = "lookAheadValue"
        val parseStack = Stack<IShiftReduceStackItem>()
        val leftRightParentheses = 0
        val leftRightBracket = 0

        val continueReducing = true
        Mockito.`when`(reductionEnder.endReduction(parseStack, listOf(operatorItem))).thenReturn(continueReducing)

        val actual = leftParenthesesOperatorReducer.reduce(
            operatorItem,
            lookAheadValue,
            parseStack,
            leftRightParentheses,
            leftRightBracket
        )

        Assertions.assertEquals(leftRightParentheses + 1, actual.leftRightParentheses)
        Assertions.assertEquals(leftRightBracket, actual.leftRightBracket)
        Assertions.assertTrue(actual.continueReducing)
        Assertions.assertFalse(actual.shouldBreak)
        Assertions.assertFalse(actual.hasNotSeenParentheses)
    }
}