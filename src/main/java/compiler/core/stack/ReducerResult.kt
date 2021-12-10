package compiler.core.stack

data class ReducerResult(
    val leftRightParentheses: Int,
    val leftRightBracket: Int,
    val continueReducing: Boolean,
    val shouldBreak: Boolean,
    val hasNotSeenParentheses: Boolean
)
