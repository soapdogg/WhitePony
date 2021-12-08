package compiler.parser.impl

import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.parser.impl.internal.IExpressionNodeReducer
import compiler.parser.impl.internal.IExpressionNodeReductionOrchestrator
import compiler.parser.impl.internal.IOperatorPrecedenceDeterminer
import compiler.parser.impl.internal.IReductionEnder

internal class PlusMinusExpressionNodeReductionOrchestrator(
    private val operatorPrecedenceDeterminer: IOperatorPrecedenceDeterminer,
    private val reductionEnder: IReductionEnder,
    private val binaryOperatorExpressionNodeReducer: IExpressionNodeReducer,
    private val unaryExpressionNodeReducer: IExpressionNodeReducer
): IExpressionNodeReductionOrchestrator {
    override fun reduce(
        lookAheadValue: String,
        leftItem: NodeShiftReduceStackItem,
        operatorItem: OperatorShiftReduceStackItem,
        parseStack: Stack<IShiftReduceStackItem>
    ): Boolean {
        val operator = operatorItem.operator
        return if (parseStack.isNotEmpty()) {
            val isLookaheadLowerPrecedence = operatorPrecedenceDeterminer.determinerIfLookaheadIsLowerPrecedenceThanCurrent(
                operator,
                lookAheadValue
            )
            if (isLookaheadLowerPrecedence) {
                reductionEnder.endReduction(parseStack, listOf(operatorItem, leftItem))
            } else {
                val node = leftItem.node
                binaryOperatorExpressionNodeReducer.reduceToExpressionNode(node, operator, parseStack)
                true
            }
        } else {
            val node = leftItem.node
            unaryExpressionNodeReducer.reduceToExpressionNode(node, operator, parseStack)
            true
        }
    }
}