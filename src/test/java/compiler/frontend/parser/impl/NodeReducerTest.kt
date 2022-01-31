package compiler.frontend.parser.impl

import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.frontend.parser.impl.internal.IExpressionNodeReducer
import compiler.frontend.parser.impl.internal.IExpressionNodeReductionOrchestrator
import compiler.frontend.parser.impl.internal.IReductionEnder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class NodeReducerTest {
    private val expressionKey = "expressionKey"
    private val expressionReducer = Mockito.mock(IExpressionNodeReducer::class.java)
    private val expressionNodeReducerMap = mapOf(expressionKey to expressionReducer)
    private val expressionNodeReductionOrchestrator = Mockito.mock(IExpressionNodeReductionOrchestrator::class.java)
    private val plusKey = "plusKey"
    private val plusMinusOperatorSet = setOf(plusKey)
    private val plusMinusExpressionNodeReductionOrchestrator = Mockito.mock(IExpressionNodeReductionOrchestrator::class.java)
    private val reductionEnder = Mockito.mock(IReductionEnder::class.java)

    private val nodeReducer = NodeReducer(
        expressionNodeReducerMap,
        expressionNodeReductionOrchestrator,
        plusMinusOperatorSet,
        plusMinusExpressionNodeReductionOrchestrator,
        reductionEnder
    )

    @Test
    fun expressionNodeTest() {
        val lookaheadValue = "lookahead"
        val nodeItem = Mockito.mock(NodeShiftReduceStackItem::class.java)
        val parseStack = Stack<IShiftReduceStackItem>()

        val operatorItem = Mockito.mock(OperatorShiftReduceStackItem::class.java)
        parseStack.push(operatorItem)

        Mockito.`when`(operatorItem.operator).thenReturn(expressionKey)

        Mockito.`when`(expressionNodeReductionOrchestrator.reduce(lookaheadValue, nodeItem, operatorItem, parseStack)).thenReturn(true)

        val actual = nodeReducer.reduce(lookaheadValue, nodeItem, parseStack)
        Assertions.assertTrue(actual)
    }

    @Test
    fun plusMinusNodeTest() {
        val lookaheadValue = "lookahead"
        val nodeItem = Mockito.mock(NodeShiftReduceStackItem::class.java)
        val parseStack = Stack<IShiftReduceStackItem>()

        val operatorItem = Mockito.mock(OperatorShiftReduceStackItem::class.java)
        parseStack.push(operatorItem)

        Mockito.`when`(operatorItem.operator).thenReturn(plusKey)

        Mockito.`when`(plusMinusExpressionNodeReductionOrchestrator.reduce(lookaheadValue, nodeItem, operatorItem, parseStack)).thenReturn(true)

        val actual = nodeReducer.reduce(lookaheadValue, nodeItem, parseStack)
        Assertions.assertTrue(actual)
    }

    @Test
    fun nodeNotInMapOrSetTest() {
        val lookaheadValue = "lookahead"
        val nodeItem = Mockito.mock(NodeShiftReduceStackItem::class.java)
        val parseStack = Stack<IShiftReduceStackItem>()

        val operatorItem = Mockito.mock(OperatorShiftReduceStackItem::class.java)
        parseStack.push(operatorItem)

        val operator = "operator"
        Mockito.`when`(operatorItem.operator).thenReturn(operator)

        Mockito.`when`(reductionEnder.endReduction(parseStack, listOf(operatorItem, nodeItem))).thenReturn(true)

        val actual = nodeReducer.reduce(lookaheadValue, nodeItem, parseStack)
        Assertions.assertTrue(actual)
    }

    @Test
    fun stackIsEmptyTest() {
        val lookaheadValue = "lookahead"
        val nodeItem = Mockito.mock(NodeShiftReduceStackItem::class.java)
        val parseStack = Stack<IShiftReduceStackItem>()

        Mockito.`when`(reductionEnder.endReduction(parseStack, listOf(nodeItem))).thenReturn(true)

        val actual = nodeReducer.reduce(lookaheadValue, nodeItem, parseStack)
        Assertions.assertTrue(actual)
    }
}