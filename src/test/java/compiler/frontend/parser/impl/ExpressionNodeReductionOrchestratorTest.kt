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

class ExpressionNodeReductionOrchestratorTest {
    private val operatorPrecedenceDeterminer = Mockito.mock(IOperatorPrecedenceDeterminer::class.java)
    private val reductionEnder = Mockito.mock(IReductionEnder::class.java)
    private val operator = "operator"
    private val binaryExpressionNodeReducer = Mockito.mock(IExpressionNodeReducer::class.java)
    private val binaryExpressionNodeReducerMap = mapOf(operator to binaryExpressionNodeReducer)

    private val expressionNodeReductionOrchestrator = ExpressionNodeReductionOrchestrator(
        operatorPrecedenceDeterminer,
        reductionEnder,
        binaryExpressionNodeReducerMap
    )

    @Test
    fun lookAheadIsLowerPrecedenceTest() {
        val lookAheadValue = "lookAheadValue"
        val leftItem = Mockito.mock(NodeShiftReduceStackItem::class.java)
        val operatorItem = Mockito.mock(OperatorShiftReduceStackItem::class.java)
        val parseStack = Stack<IShiftReduceStackItem>()

        Mockito.`when`(operatorItem.operator).thenReturn(operator)

        val isLookAheadLowerPrecedence = true
        Mockito.`when`(operatorPrecedenceDeterminer.determinerIfLookaheadIsLowerPrecedenceThanCurrent(operator, lookAheadValue)).thenReturn(isLookAheadLowerPrecedence)

        val result = true
        Mockito.`when`(reductionEnder.endReduction(parseStack, listOf(operatorItem, leftItem))).thenReturn(result)

        val actual = expressionNodeReductionOrchestrator.reduce(
            lookAheadValue, leftItem, operatorItem, parseStack
        )
        Assertions.assertTrue(actual)
    }

    @Test
    fun lookAheadIsNotLowerPrecedenceTest() {
        val lookAheadValue = "lookAheadValue"
        val leftItem = Mockito.mock(NodeShiftReduceStackItem::class.java)
        val operatorItem = Mockito.mock(OperatorShiftReduceStackItem::class.java)
        val parseStack = Stack<IShiftReduceStackItem>()

        Mockito.`when`(operatorItem.operator).thenReturn(operator)

        val isLookAheadLowerPrecedence = false
        Mockito.`when`(operatorPrecedenceDeterminer.determinerIfLookaheadIsLowerPrecedenceThanCurrent(operator, lookAheadValue)).thenReturn(isLookAheadLowerPrecedence)

        val node = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(leftItem.node).thenReturn(node)

        val actual = expressionNodeReductionOrchestrator.reduce(
            lookAheadValue, leftItem, operatorItem, parseStack
        )
        Assertions.assertTrue(actual)
        Mockito.verify(binaryExpressionNodeReducer).reduceToExpressionNode(node, operator, parseStack)
    }
}