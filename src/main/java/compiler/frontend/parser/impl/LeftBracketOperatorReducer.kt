package compiler.frontend.parser.impl

import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.ReducerResult
import compiler.core.stack.Stack
import compiler.frontend.parser.impl.internal.IOperatorReducer
import compiler.frontend.parser.impl.internal.IReductionEnder

internal class LeftBracketOperatorReducer(
    private val reductionEnder: IReductionEnder
): IOperatorReducer {
    override fun reduce(
        operatorItem: OperatorShiftReduceStackItem,
        lookAheadValue: String,
        parseStack: Stack<IShiftReduceStackItem>,
        leftRightParentheses: Int,
        leftRightBracket: Int
    ): ReducerResult {
        val continueReducing = reductionEnder.endReduction(parseStack, listOf(operatorItem))
        return ReducerResult(
            leftRightParentheses,
            leftRightBracket + 1,
            continueReducing,
            shouldBreak = false,
            hasNotSeenParentheses = true
        )
    }
}