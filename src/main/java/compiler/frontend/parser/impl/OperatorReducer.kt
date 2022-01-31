package compiler.frontend.parser.impl

import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.ReducerResult
import compiler.core.stack.Stack
import compiler.frontend.parser.impl.internal.IOperatorReducer
import compiler.frontend.parser.impl.internal.IReductionEnder

internal class OperatorReducer(
    private val operatorReducers: Map<String, IOperatorReducer>,
    private val reductionEnder: IReductionEnder
): IOperatorReducer {
    override fun reduce(
        operatorItem: OperatorShiftReduceStackItem,
        lookAheadValue: String,
        parseStack: Stack<IShiftReduceStackItem>,
        leftRightParentheses: Int,
        leftRightBracket: Int
    ): ReducerResult {
        return if (operatorReducers.containsKey(operatorItem.operator)) {
            val reducer = operatorReducers.getValue(operatorItem.operator)
            reducer.reduce(operatorItem, lookAheadValue, parseStack, leftRightParentheses, leftRightBracket)
        }
        else {
            val continueReducing = reductionEnder.endReduction(parseStack, listOf(operatorItem))
            return ReducerResult(
                leftRightParentheses,
                leftRightBracket,
                continueReducing,
                shouldBreak = false,
                hasNotSeenParentheses = true
            )
        }
    }
}