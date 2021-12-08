package compiler.parser.impl

import compiler.core.constants.TokenizerConstants
import compiler.core.nodes.parsed.*
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.INodeReducer
import compiler.parser.impl.internal.IReductionEnder
import compiler.parser.impl.internal.IShifter

internal class ShiftReduceExpressionParser(
    private val shifter: IShifter,
    private val nodeReducer: INodeReducer,
    private val reductionEnder: IReductionEnder,
    private val acceptedTokenTypes: Set<TokenType>,
    private val operators: Set<String>,
    private val binaryArrayExpressionNodeReducer: BinaryArrayExpressionNodeReducer,
    private val innerExpressionNodeReducer: InnerExpressionNodeReducer,
    private val unaryPostExpressionNodeReducer: UnaryPostExpressionNodeReducer
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

                    when (top.operator) {
                        TokenizerConstants.RIGHT_PARENTHESES -> {
                            --leftRightParentheses
                            if (leftRightParentheses < 0) {
                                currentPosition--
                                break@top
                            }
                            hasNotSeenParentheses = operators.contains(lookAhead.value)
                            val nodeItem = parseStack.pop() as NodeShiftReduceStackItem
                            innerExpressionNodeReducer.reduceToExpressionNode(nodeItem.node, top.operator, parseStack)
                        }
                        TokenizerConstants.LEFT_PARENTHESES -> {
                            ++leftRightParentheses
                            hasNotSeenParentheses = false
                            continueReducing = reductionEnder.endReduction(parseStack, listOf(top))
                        }
                        TokenizerConstants.RIGHT_BRACKET -> {
                            --leftRightBracket
                            if (leftRightBracket < 0) {
                                currentPosition--
                                break@top
                            }
                            val nodeItem = parseStack.pop() as NodeShiftReduceStackItem
                            binaryArrayExpressionNodeReducer.reduceToExpressionNode(nodeItem.node, top.operator, parseStack)
                        }
                        TokenizerConstants.LEFT_BRACKET -> {
                            ++leftRightBracket
                            continueReducing = reductionEnder.endReduction(parseStack, listOf(top))
                        }
                        TokenizerConstants.INCREMENT, TokenizerConstants.DECREMENT -> {
                            if (parseStack.isNotEmpty()) {
                                val nodeItem = parseStack.pop()
                                if (nodeItem is NodeShiftReduceStackItem) {
                                    unaryPostExpressionNodeReducer.reduceToExpressionNode(nodeItem.node, top.operator, parseStack)
                                } else {
                                    continueReducing = reductionEnder.endReduction(parseStack, listOf(nodeItem, top))
                                }
                            } else {
                                continueReducing = reductionEnder.endReduction(parseStack, listOf(top))
                            }
                        }
                        else -> {
                            continueReducing = reductionEnder.endReduction(parseStack, listOf(top))
                        }
                    }
                }
            }
        } while (acceptedTokenTypes.contains(lookAhead.type)
            && (hasNotSeenParentheses || leftRightParentheses > 0)
        )

        val resultStackItem = parseStack.pop() as NodeShiftReduceStackItem
        return Pair(resultStackItem.node, currentPosition)
    }
}