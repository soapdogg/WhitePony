package compiler.frontend.parser.impl

import compiler.core.stack.*
import compiler.frontend.parser.impl.internal.IExpressionNodeReducer
import compiler.frontend.parser.impl.internal.IOperatorReducer

internal class RightBracketOperatorReducer(
    private val binaryArrayExpressionNodeReducer: IExpressionNodeReducer
): IOperatorReducer {
    override fun reduce(
        operatorItem: OperatorShiftReduceStackItem,
        lookAheadValue: String,
        parseStack: Stack<IShiftReduceStackItem>,
        leftRightParentheses: Int,
        leftRightBracket: Int
    ): ReducerResult {
        val updatedLeftRightBracket = leftRightBracket - 1
        val shouldBreak = updatedLeftRightBracket < 0
        val continueReducing = true

        if (!shouldBreak) {
            val nodeItem = parseStack.pop() as NodeShiftReduceStackItem
            binaryArrayExpressionNodeReducer.reduceToExpressionNode(
                nodeItem.node,
                operatorItem.operator,
                parseStack
            )
            return ReducerResult(
                leftRightParentheses,
                updatedLeftRightBracket,
                continueReducing,
                shouldBreak,
                true
            )
        }
        return ReducerResult(
            leftRightParentheses,
            updatedLeftRightBracket,
            continueReducing,
            shouldBreak,
            continueReducing
        )
    }
}