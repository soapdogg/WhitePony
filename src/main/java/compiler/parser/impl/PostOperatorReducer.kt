package compiler.parser.impl

import compiler.core.stack.*
import compiler.parser.impl.internal.IExpressionNodeReducer
import compiler.parser.impl.internal.IOperatorReducer
import compiler.parser.impl.internal.IReductionEnder

internal class PostOperatorReducer(
    private val unaryPostExpressionNodeReducer: IExpressionNodeReducer,
    private val reductionEnder: IReductionEnder
): IOperatorReducer {
    override fun reduce(
        operatorItem: OperatorShiftReduceStackItem,
        lookAheadValue: String,
        parseStack: Stack<IShiftReduceStackItem>,
        leftRightParentheses: Int,
        leftRightBracket: Int
    ): ReducerResult {
        val continueReducing = if (parseStack.isNotEmpty()) {
            val nodeItem = parseStack.pop()
            if (nodeItem is NodeShiftReduceStackItem) {
                unaryPostExpressionNodeReducer.reduceToExpressionNode(nodeItem.node, operatorItem.operator, parseStack)
                true
            } else {
                reductionEnder.endReduction(parseStack, listOf(nodeItem, operatorItem))
            }
        } else {
            reductionEnder.endReduction(parseStack, listOf(operatorItem))
        }
        return ReducerResult(
            leftRightParentheses,
            leftRightBracket,
            continueReducing,
            shouldBreak = false,
            hasNotSeenParentheses = true
        )
    }
}