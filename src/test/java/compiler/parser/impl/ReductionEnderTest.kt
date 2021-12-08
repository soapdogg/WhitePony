package compiler.parser.impl

import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.Stack
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ReductionEnderTest {
    private val reductionEnder = ReductionEnder()

    @Test
    fun endReductionTest() {
        val parseStack = Stack<IShiftReduceStackItem>()
        val item = Mockito.mock(IShiftReduceStackItem::class.java)
        val itemsToPush = listOf(item)

        val actual = reductionEnder.endReduction(parseStack, itemsToPush)
        Assertions.assertFalse(actual)
        val top = parseStack.pop()
        Assertions.assertEquals(item, top)
    }
}