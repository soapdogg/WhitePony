package compiler.parser.impl

import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.parser.impl.internal.IExpressionNodeReducer
import compiler.parser.impl.internal.IExpressionNodeReductionOrchestrator
import compiler.parser.impl.internal.INodeReducer
import compiler.parser.impl.internal.IReductionEnder

internal class NodeReducer(
    private val expressionNodeReducerMap: Map<String, IExpressionNodeReducer>,
    private val expressionNodeReductionOrchestrator: IExpressionNodeReductionOrchestrator,
    private val plusMinusOperatorSet: Set<String>,
    private val plusMinusExpressionNodeReductionOrchestrator: IExpressionNodeReductionOrchestrator,
    private val reductionEnder: IReductionEnder
): INodeReducer {
    override fun reduce(
        lookAheadValue: String,
        nodeItem: NodeShiftReduceStackItem,
        parseStack: Stack<IShiftReduceStackItem>
    ): Boolean {
        return if (parseStack.isNotEmpty()) {
            val operatorItem = parseStack.pop() as OperatorShiftReduceStackItem
            val operator = operatorItem.operator

            if (expressionNodeReducerMap.containsKey(operator)) {
                expressionNodeReductionOrchestrator.reduce(lookAheadValue, nodeItem, operatorItem, parseStack)
            } else if (plusMinusOperatorSet.contains(operator)) {
                plusMinusExpressionNodeReductionOrchestrator.reduce(lookAheadValue, nodeItem, operatorItem, parseStack)
            } else {
                reductionEnder.endReduction(parseStack, listOf(operatorItem, nodeItem))
            }
        } else {
            reductionEnder.endReduction(parseStack, listOf(nodeItem))
        }
    }
}