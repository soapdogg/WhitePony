package compiler.parser.impl

import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.parser.impl.internal.IExpressionNodeReducer
import compiler.parser.impl.internal.IExpressionNodeReductionOrchestrator
import compiler.parser.impl.internal.IOperatorPrecedenceDeterminer
import compiler.parser.impl.internal.IReductionEnder

internal class ExpressionNodeReductionOrchestrator(
    private val operatorPrecedenceDeterminer: IOperatorPrecedenceDeterminer,
    private val reductionEnder: IReductionEnder,
    private val binaryExpressionNodeReducerMap: Map<String, IExpressionNodeReducer>
): IExpressionNodeReductionOrchestrator {
    override fun reduce(
        lookAheadValue: String,
        leftItem: NodeShiftReduceStackItem,
        operatorItem: OperatorShiftReduceStackItem,
        parseStack: Stack<IShiftReduceStackItem>
    ): Boolean {
        val operator = operatorItem.operator
        val isLookaheadLowerPrecedence = operatorPrecedenceDeterminer.determinerIfLookaheadIsLowerPrecedenceThanCurrent(
            operator,
            lookAheadValue
        )
        return if (isLookaheadLowerPrecedence) {
            reductionEnder.endReduction(parseStack, listOf(operatorItem, leftItem))
        } else {
            val binaryExpressionReducer = binaryExpressionNodeReducerMap.getValue(operator)
            val node = leftItem.node
            binaryExpressionReducer.reduceToExpressionNode(
                node,
                operator,
                parseStack
            )
            return true
        }
    }
}