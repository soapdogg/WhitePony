package compiler.parser.impl.internal

import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.ReducerResult
import compiler.core.stack.Stack

internal interface IReducer {
    fun reduce(
        lookAheadValue: String,
        parseStack: Stack<IShiftReduceStackItem>,
        leftRightParentheses: Int,
        leftRightBracket: Int,
        hasNotSeenParentheses: Boolean
    ): ReducerResult
}