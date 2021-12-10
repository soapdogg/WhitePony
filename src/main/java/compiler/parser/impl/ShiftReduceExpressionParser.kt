package compiler.parser.impl

import compiler.core.nodes.parsed.*
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.INodeReducer
import compiler.parser.impl.internal.IOperatorReducer
import compiler.parser.impl.internal.IShifter

internal class ShiftReduceExpressionParser(
    private val shifter: IShifter,
    private val nodeReducer: INodeReducer,
    private val operatorReducer: IOperatorReducer,
    private val acceptedTokenTypes: Set<TokenType>
): IExpressionParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val parseStack = Stack<IShiftReduceStackItem>()
        var currentPosition = startingPosition
        var hasNotSeenParentheses = true
        var leftRightParentheses = 0
        var leftRightBracket = 0

        top@ do {
            currentPosition = shifter.shift(tokens, currentPosition, parseStack)

            val lookAhead = tokens[currentPosition]

            //Reduce
            var continueReducing = true
            while(continueReducing) {
                val top = parseStack.pop()
                if (top is NodeShiftReduceStackItem) {
                    continueReducing = nodeReducer.reduce(lookAhead.value, top, parseStack)
                }
                else {
                    top as OperatorShiftReduceStackItem
                    val reducerResult = operatorReducer.reduce(top, lookAhead.value, parseStack, leftRightParentheses, leftRightBracket)
                    if (reducerResult.shouldBreak) {
                        --currentPosition
                        break@top
                    }
                    leftRightParentheses = reducerResult.leftRightParentheses
                    leftRightBracket = reducerResult.leftRightBracket
                    continueReducing = reducerResult.continueReducing
                    hasNotSeenParentheses = reducerResult.hasNotSeenParentheses
                }
            }
        } while (acceptedTokenTypes.contains(lookAhead.type)
            && (hasNotSeenParentheses || leftRightParentheses > 0)
        )

        val resultStackItem = parseStack.pop() as NodeShiftReduceStackItem
        return Pair(resultStackItem.node, currentPosition)
    }
}