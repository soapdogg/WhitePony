package compiler.frontend.parser.impl

import compiler.core.stack.*
import compiler.frontend.parser.impl.internal.IExpressionNodeReducer
import compiler.frontend.parser.impl.internal.IOperatorReducer

internal class RightParenthesesOperatorReducer(
    private val innerExpressionNodeReducer: IExpressionNodeReducer,
    private val operators: Set<String>
): IOperatorReducer {
    override fun reduce(
        operatorItem: OperatorShiftReduceStackItem,
        lookAheadValue: String,
        parseStack: Stack<IShiftReduceStackItem>,
        leftRightParentheses: Int,
        leftRightBracket: Int,
    ): ReducerResult {
        val updatedLeftRightParentheses = leftRightParentheses - 1
        val shouldBreak = updatedLeftRightParentheses < 0
        val continueReducing = true

        if (!shouldBreak) {
            val hasNotSeenParentheses = operators.contains(lookAheadValue)
            val nodeItem = parseStack.pop() as NodeShiftReduceStackItem
            innerExpressionNodeReducer.reduceToExpressionNode(
                nodeItem.node,
                operatorItem.operator,
                parseStack
            )
            return ReducerResult(
                updatedLeftRightParentheses,
                leftRightBracket,
                continueReducing,
                shouldBreak,
                hasNotSeenParentheses
            )
        }
        return ReducerResult(
            updatedLeftRightParentheses,
            leftRightBracket,
            continueReducing,
            shouldBreak,
            continueReducing
        )

    }
}