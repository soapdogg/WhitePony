package compiler.frontend.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.frontend.parser.impl.internal.IExpressionNodeReducer
import compiler.frontend.parser.impl.internal.IReductionEnder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class PostOperatorReducerTest {
    private val unaryPostExpressionNodeReducer = Mockito.mock(IExpressionNodeReducer::class.java)
    private val reductionEnder = Mockito.mock(IReductionEnder::class.java)

    private val postOperatorReducer = PostOperatorReducer(
        unaryPostExpressionNodeReducer,
        reductionEnder
    )

    @Test
    fun topIsNodeTest() {
        val operatorItem = Mockito.mock(OperatorShiftReduceStackItem::class.java)
        val lookAheadValue = "lookAheadValue"
        val parseStack = Stack<IShiftReduceStackItem>()
        val leftRightParentheses = 0
        val leftRightBracket = 0

        val nodeItem = Mockito.mock(NodeShiftReduceStackItem::class.java)
        parseStack.push(nodeItem)

        val node = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(nodeItem.node).thenReturn(node)

        val operator = "operator"
        Mockito.`when`(operatorItem.operator).thenReturn(operator)

        val actual = postOperatorReducer.reduce(
            operatorItem,
            lookAheadValue,
            parseStack,
            leftRightParentheses,
            leftRightBracket
        )
        Assertions.assertEquals(leftRightParentheses, actual.leftRightParentheses)
        Assertions.assertEquals(leftRightBracket, actual.leftRightBracket)
        Assertions.assertTrue(actual.continueReducing)
        Assertions.assertFalse(actual.shouldBreak)
        Assertions.assertTrue(actual.hasNotSeenParentheses)
        Mockito.verify(unaryPostExpressionNodeReducer).reduceToExpressionNode(
            node,
            operator,
            parseStack
        )
    }

    @Test
    fun topIsNotNodeTest() {
        val operatorItem = Mockito.mock(OperatorShiftReduceStackItem::class.java)
        val lookAheadValue = "lookAheadValue"
        val parseStack = Stack<IShiftReduceStackItem>()
        val leftRightParentheses = 0
        val leftRightBracket = 0

        val nodeItem = Mockito.mock(OperatorShiftReduceStackItem::class.java)
        parseStack.push(nodeItem)

        val continueReducing = true
        Mockito.`when`(reductionEnder.endReduction(parseStack, listOf(nodeItem, operatorItem))).thenReturn(continueReducing)

        val actual = postOperatorReducer.reduce(
            operatorItem,
            lookAheadValue,
            parseStack,
            leftRightParentheses,
            leftRightBracket
        )
        Assertions.assertEquals(leftRightParentheses, actual.leftRightParentheses)
        Assertions.assertEquals(leftRightBracket, actual.leftRightBracket)
        Assertions.assertTrue(actual.continueReducing)
        Assertions.assertFalse(actual.shouldBreak)
        Assertions.assertTrue(actual.hasNotSeenParentheses)
    }

    @Test
    fun noTopNodeTest() {
        val operatorItem = Mockito.mock(OperatorShiftReduceStackItem::class.java)
        val lookAheadValue = "lookAheadValue"
        val parseStack = Stack<IShiftReduceStackItem>()
        val leftRightParentheses = 0
        val leftRightBracket = 0


        val continueReducing = true
        Mockito.`when`(reductionEnder.endReduction(parseStack, listOf(operatorItem))).thenReturn(continueReducing)

        val actual = postOperatorReducer.reduce(
            operatorItem,
            lookAheadValue,
            parseStack,
            leftRightParentheses,
            leftRightBracket
        )
        Assertions.assertEquals(leftRightParentheses, actual.leftRightParentheses)
        Assertions.assertEquals(leftRightBracket, actual.leftRightBracket)
        Assertions.assertTrue(actual.continueReducing)
        Assertions.assertFalse(actual.shouldBreak)
        Assertions.assertTrue(actual.hasNotSeenParentheses)
    }
}