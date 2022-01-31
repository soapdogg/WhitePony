package compiler.frontend.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.frontend.parser.impl.internal.IExpressionNodeReducer
import compiler.frontend.parser.impl.internal.IOperatorPrecedenceDeterminer
import compiler.frontend.parser.impl.internal.IReductionEnder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class PlusMinusExpressionNodeReductionOrchestratorTest {
    private val operatorPrecedenceDeterminer = Mockito.mock(IOperatorPrecedenceDeterminer::class.java)
    private val reductionEnder = Mockito.mock(IReductionEnder::class.java)
    private val binaryOperatorExpressionNodeReducer = Mockito.mock(IExpressionNodeReducer::class.java)
    private val unaryExpressionNodeReducer = Mockito.mock(IExpressionNodeReducer::class.java)

    private val plusMinusExpressionNodeReductionOrchestrator = PlusMinusExpressionNodeReductionOrchestrator(
        operatorPrecedenceDeterminer, reductionEnder, binaryOperatorExpressionNodeReducer, unaryExpressionNodeReducer
    )

    @Test
    fun lookaheadLowerPrecedenceTest() {
        val lookAheadValue = "lookAheadValue"
        val leftItem = Mockito.mock(NodeShiftReduceStackItem::class.java)
        val operatorItem = Mockito.mock(OperatorShiftReduceStackItem::class.java)
        val parseStack = Stack<IShiftReduceStackItem>()

        val rightItem = Mockito.mock(IShiftReduceStackItem::class.java)
        parseStack.push(rightItem)

        val operator = "operator"
        Mockito.`when`(operatorItem.operator).thenReturn(operator)

        val isLookAheadLowerPrecedence = true
        Mockito.`when`(operatorPrecedenceDeterminer.determinerIfLookaheadIsLowerPrecedenceThanCurrent(operator, lookAheadValue)).thenReturn(isLookAheadLowerPrecedence)

        val result = true
        Mockito.`when`(reductionEnder.endReduction(parseStack, listOf(operatorItem, leftItem))).thenReturn(result)

        val actual = plusMinusExpressionNodeReductionOrchestrator.reduce(
            lookAheadValue, leftItem, operatorItem, parseStack
        )
        Assertions.assertTrue(actual)
    }

    @Test
    fun lookaheadIsNotLowerPrecedenceTest() {
        val lookAheadValue = "lookAheadValue"
        val leftItem = Mockito.mock(NodeShiftReduceStackItem::class.java)
        val operatorItem = Mockito.mock(OperatorShiftReduceStackItem::class.java)
        val parseStack = Stack<IShiftReduceStackItem>()

        val rightItem = Mockito.mock(IShiftReduceStackItem::class.java)
        parseStack.push(rightItem)

        val operator = "operator"
        Mockito.`when`(operatorItem.operator).thenReturn(operator)

        val isLookAheadLowerPrecedence = false
        Mockito.`when`(operatorPrecedenceDeterminer.determinerIfLookaheadIsLowerPrecedenceThanCurrent(operator, lookAheadValue)).thenReturn(isLookAheadLowerPrecedence)

        val node = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(leftItem.node).thenReturn(node)

        val actual = plusMinusExpressionNodeReductionOrchestrator.reduce(
            lookAheadValue, leftItem, operatorItem, parseStack
        )
        Assertions.assertTrue(actual)
        Mockito.verify(binaryOperatorExpressionNodeReducer).reduceToExpressionNode(node, operator, parseStack)
    }

    @Test
    fun stackIsEmptyTest() {
        val lookAheadValue = "lookAheadValue"
        val leftItem = Mockito.mock(NodeShiftReduceStackItem::class.java)
        val operatorItem = Mockito.mock(OperatorShiftReduceStackItem::class.java)
        val parseStack = Stack<IShiftReduceStackItem>()

        val operator = "operator"
        Mockito.`when`(operatorItem.operator).thenReturn(operator)

        val node = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(leftItem.node).thenReturn(node)

        val actual = plusMinusExpressionNodeReductionOrchestrator.reduce(
            lookAheadValue, leftItem, operatorItem, parseStack
        )
        Assertions.assertTrue(actual)
        Mockito.verify(unaryExpressionNodeReducer).reduceToExpressionNode(node, operator, parseStack)
    }
}