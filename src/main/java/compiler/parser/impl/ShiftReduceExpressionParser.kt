package compiler.parser.impl

import compiler.core.constants.TokenizerConstants
import compiler.core.nodes.parsed.*
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IBinaryExpressionNodeReducer
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.IOperatorPrecedenceDeterminer
import compiler.parser.impl.internal.IShifter

internal class ShiftReduceExpressionParser(
    private val shifter: IShifter,
    private val binaryExpressionNodeReducerMap: Map<String, IBinaryExpressionNodeReducer>,
    private val operatorPrecedenceDeterminer: IOperatorPrecedenceDeterminer,
    private val acceptedTokenTypes: Set<TokenType>,
    private val binaryOperatorExpressionNodeReducer: BinaryOperatorExpressionNodeReducer
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

                        if (binaryExpressionNodeReducerMap.containsKey(operatorItem.operator)) {
                            val operator = operatorItem.operator
                            val isLookaheadLowerPrecedence = operatorPrecedenceDeterminer.determinerIfLookaheadIsLowerPrecedenceThanCurrent(
                                operator,
                                lookAhead.value
                            )
                            if (isLookaheadLowerPrecedence) {
                                parseStack.push(operatorItem)
                                parseStack.push(top)
                                canReduce = false
                            } else {
                                val binaryExpressionReducer = binaryExpressionNodeReducerMap.getValue(operator)
                                binaryExpressionReducer.reduceToBinaryNode(
                                    node,
                                    operatorItem.operator,
                                    parseStack
                                )
                            }
                        } else {
                            when (operatorItem.operator) {
                                TokenizerConstants.INCREMENT, TokenizerConstants.DECREMENT -> {
                                    if (
                                        operatorPrecedenceDeterminer.determinerIfLookaheadIsLowerPrecedenceThanCurrent(operatorItem.operator, lookAhead.value)
                                    ) {
                                        parseStack.push(operatorItem)
                                        parseStack.push(top)
                                        canReduce = false
                                    } else {
                                        val resultNode = ParsedUnaryPreOperatorExpressionNode(
                                            node,
                                            operatorItem.operator[0].toString()
                                        )
                                        parseStack.push(NodeShiftReduceStackItem(resultNode))
                                    }
                                }
                                TokenizerConstants.NEGATION -> {
                                    val resultNode = ParsedUnaryNotOperatorExpressionNode(node)
                                    parseStack.push(NodeShiftReduceStackItem(resultNode))
                                }
                                TokenizerConstants.BIT_NEGATION -> {
                                    val resultNode = ParsedUnaryExpressionNode(
                                        node,
                                        operatorItem.operator
                                    )
                                    parseStack.push(NodeShiftReduceStackItem(resultNode))
                                }
                                TokenizerConstants.MINUS_OPERATOR, TokenizerConstants.PLUS_OPERATOR -> {
                                    if (parseStack.isNotEmpty()) {
                                        if (
                                            operatorPrecedenceDeterminer.determinerIfLookaheadIsLowerPrecedenceThanCurrent(operatorItem.operator, lookAhead.value)
                                        ) {
                                            parseStack.push(operatorItem)
                                            parseStack.push(top)
                                            canReduce = false
                                        } else {
                                            binaryOperatorExpressionNodeReducer.reduceToBinaryNode(node, operatorItem.operator, parseStack)
                                        }
                                    } else {
                                        val resultNode = ParsedUnaryExpressionNode(node, operatorItem.operator)
                                        parseStack.push(NodeShiftReduceStackItem(resultNode))
                                    }
                                }
                                else -> {
                                    parseStack.push(operatorItem)
                                    parseStack.push(top)
                                    canReduce = false
                                }
                            }
                        }
                    }
                    else {
                        parseStack.push(top)
                        canReduce = false
                    }
                }
                else {
                    top as OperatorShiftReduceStackItem
                    val operator = top.operator

                    if (operator == TokenizerConstants.RIGHT_PARENTHESES) {
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
                    else if (operator == TokenizerConstants.LEFT_PARENTHESES) {
                        ++leftRightParentheses
                        hasNotSeenParentheses = false
                        parseStack.push(top)
                        canReduce = false
                    }
                    else if (operator == TokenizerConstants.RIGHT_BRACKET) {
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
                    else if (operator == TokenizerConstants.LEFT_BRACKET) {
                        ++leftRightBracket
                        parseStack.push(top)
                        canReduce = false
                    }
                    else if (operator == TokenizerConstants.INCREMENT) {
                        if (parseStack.isNotEmpty()) {
                            val nodeItem = parseStack.pop()
                            if (nodeItem is NodeShiftReduceStackItem) {
                                val resultNode = ParsedUnaryPostOperatorExpressionNode(nodeItem.node, TokenizerConstants.PLUS_OPERATOR, TokenizerConstants.MINUS_OPERATOR)
                                parseStack.push(NodeShiftReduceStackItem(resultNode))
                            }
                            else {
                                parseStack.push(nodeItem)
                                parseStack.push(top)
                                canReduce = false
                            }
                        } else {
                            parseStack.push(top)
                            canReduce = false
                        }
                    }
                    else {
                        parseStack.push(top)
                        canReduce = false
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