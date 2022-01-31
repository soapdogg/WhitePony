package compiler.frontend.parser.impl

import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.ReducerResult
import compiler.core.stack.Stack
import compiler.frontend.parser.impl.internal.IOperatorReducer
import compiler.frontend.parser.impl.internal.IReductionEnder

internal class LeftParenthesesOperatorReducer(
    private val reductionEnder: IReductionEnder
) : IOperatorReducer {
    override fun reduce(
        operatorItem: OperatorShiftReduceStackItem,
        lookAheadValue: String,
        parseStack: Stack<IShiftReduceStackItem>,
        leftRightParentheses: Int,
        leftRightBracket: Int
    ): ReducerResult {
        val continueReducing = reductionEnder.endReduction(parseStack, listOf(operatorItem))
        return ReducerResult(
            leftRightParentheses + 1,
            leftRightBracket,
            continueReducing,
            shouldBreak = false,
            hasNotSeenParentheses = false
        )
    }
}