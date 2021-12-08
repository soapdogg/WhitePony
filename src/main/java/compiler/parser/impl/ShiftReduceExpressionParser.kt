package compiler.parser.impl

import compiler.core.constants.TokenizerConstants
import compiler.core.nodes.parsed.*
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IExpressionNodeReducer
import compiler.parser.impl.internal.IExpressionNodeReductionOrchestrator
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.IOperatorPrecedenceDeterminer
import compiler.parser.impl.internal.IReductionEnder
import compiler.parser.impl.internal.IShifter

internal class ShiftReduceExpressionParser(
    private val shifter: IShifter,
    private val expressionNodeReductionOrchestrator: IExpressionNodeReductionOrchestrator,
    private val expressionNodeReducerMap: Map<String, IExpressionNodeReducer>,
    private val plusMinusOperatorSet: Set<String>,
    private val reductionEnder: IReductionEnder,
    private val operatorPrecedenceDeterminer: IOperatorPrecedenceDeterminer,
    private val acceptedTokenTypes: Set<TokenType>,
    private val binaryOperatorExpressionNodeReducer: BinaryOperatorExpressionNodeReducer,
    private val unaryExpressionNodeReducer: UnaryExpressionNodeReducer
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
            var canReduce = true
            while(canReduce) {
                val top = parseStack.pop()
                if (top is NodeShiftReduceStackItem) {
                    val node = top.node
                    if (parseStack.isNotEmpty()) {
                        val operatorItem = parseStack.pop() as OperatorShiftReduceStackItem
                        val operator = operatorItem.operator

                        if (expressionNodeReducerMap.containsKey(operator)) {
                            canReduce = expressionNodeReductionOrchestrator.reduce(lookAhead.value, top, operatorItem, parseStack)
                        } else if (plusMinusOperatorSet.contains(operator)) {
                            if (parseStack.isNotEmpty()) {
                                if (
                                    operatorPrecedenceDeterminer.determinerIfLookaheadIsLowerPrecedenceThanCurrent(operator, lookAhead.value)
                                ) {
                                    canReduce = reductionEnder.endReduction(parseStack, listOf(operatorItem, top))
                                } else {
                                    binaryOperatorExpressionNodeReducer.reduceToExpressionNode(node, operator, parseStack)
                                }
                            } else {
                                unaryExpressionNodeReducer.reduceToExpressionNode(node, operator, parseStack)
                            }
                        } else {
                            canReduce = reductionEnder.endReduction(parseStack, listOf(operatorItem, top))
                        }
                    }
                    else {
                        canReduce = reductionEnder.endReduction(parseStack, listOf(top))
                    }
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
                            hasNotSeenParentheses = lookAhead.value == TokenizerConstants.MINUS_OPERATOR //TODO this is hack
                            val nodeItem = parseStack.pop() as NodeShiftReduceStackItem
                            parseStack.pop() //LEFT_PARENTHESES
                            parseStack.push(NodeShiftReduceStackItem(ParsedInnerExpressionNode(nodeItem.node)))
                        }
                        TokenizerConstants.LEFT_PARENTHESES -> {
                            ++leftRightParentheses
                            hasNotSeenParentheses = false
                            canReduce = reductionEnder.endReduction(parseStack, listOf(top))
                        }
                        TokenizerConstants.RIGHT_BRACKET -> {
                            --leftRightBracket
                            if (leftRightBracket < 0) {
                                currentPosition--
                                break@top
                            }
                            val nodeItem = parseStack.pop() as NodeShiftReduceStackItem
                            parseStack.pop() //LEFT_BRACKET
                            val variableItem = parseStack.pop() as NodeShiftReduceStackItem
                            parseStack.push(NodeShiftReduceStackItem(ParsedBinaryArrayExpressionNode(variableItem.node as ParsedVariableExpressionNode, nodeItem.node)))
                        }
                        TokenizerConstants.LEFT_BRACKET -> {
                            ++leftRightBracket
                            canReduce = reductionEnder.endReduction(parseStack, listOf(top))
                        }
                        TokenizerConstants.INCREMENT -> {
                            if (parseStack.isNotEmpty()) {
                                val nodeItem = parseStack.pop()
                                if (nodeItem is NodeShiftReduceStackItem) {
                                    val resultNode = ParsedUnaryPostOperatorExpressionNode(nodeItem.node, TokenizerConstants.PLUS_OPERATOR, TokenizerConstants.MINUS_OPERATOR)
                                    parseStack.push(NodeShiftReduceStackItem(resultNode))
                                } else {
                                    canReduce = reductionEnder.endReduction(parseStack, listOf(nodeItem, top))
                                }
                            } else {
                                canReduce = reductionEnder.endReduction(parseStack, listOf(top))
                            }
                        }
                        else -> {
                            canReduce = reductionEnder.endReduction(parseStack, listOf(top))
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