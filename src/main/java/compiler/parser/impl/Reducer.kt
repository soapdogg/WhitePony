package compiler.parser.impl

import compiler.core.stack.*
import compiler.parser.impl.internal.INodeReducer
import compiler.parser.impl.internal.IOperatorReducer
import compiler.parser.impl.internal.IReducer

internal class Reducer(
    private val nodeReducer: INodeReducer,
    private val operatorReducer: IOperatorReducer
): IReducer {
    override fun reduce(
        lookAheadValue: String,
        parseStack: Stack<IShiftReduceStackItem>,
        leftRightParentheses: Int,
        leftRightBracket: Int,
        hasNotSeenParentheses: Boolean
    ): ReducerResult {
        var notSeenParentheses = hasNotSeenParentheses
        var lRP = leftRightParentheses
        var lRB = leftRightBracket
        var shouldBreak = false

        var continueReducing = true
        while(continueReducing) {
            val top = parseStack.pop()
            if (top is NodeShiftReduceStackItem) {
                continueReducing = nodeReducer.reduce(lookAheadValue, top, parseStack)
            }
            else {
                top as OperatorShiftReduceStackItem
                val reducerResult = operatorReducer.reduce(top, lookAheadValue, parseStack, lRP, lRB)
                lRP = reducerResult.leftRightParentheses
                lRB = reducerResult.leftRightBracket
                shouldBreak = reducerResult.shouldBreak
                continueReducing = reducerResult.continueReducing
                notSeenParentheses = reducerResult.hasNotSeenParentheses
            }
        }
        return ReducerResult(
            lRP,
            lRB,
            false,
            shouldBreak,
            notSeenParentheses
        )
    }
}