package compiler.parser.impl

import compiler.core.constants.PrinterConstants
import compiler.core.constants.TokenizerConstants
import compiler.core.nodes.parsed.*
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.OperatorShiftReduceStackItem
import compiler.core.stack.Stack
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IExpressionParser

internal class ShiftReduceExpressionParser(
    private val acceptedTokenTypes: Set<TokenType>
): IExpressionParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val parseStack = Stack<IShiftReduceStackItem>()
        var currentPosition = startingPosition
        var lookAhead = tokens[currentPosition]
        var hasNotSeenParentheses = true
        var leftRight = 0

        do {
            //Shift
            val stackItem = when (lookAhead.type) {
                TokenType.INTEGER, TokenType.FLOATING_POINT -> {
                    val type = if (lookAhead.type == TokenType.INTEGER) PrinterConstants.INT else PrinterConstants.DOUBLE
                    NodeShiftReduceStackItem(ParsedConstantExpressionNode(lookAhead.value, type))
                }
                TokenType.IDENTIFIER -> {
                    NodeShiftReduceStackItem(ParsedVariableExpressionNode(lookAhead.value))
                }
                else -> {
                    OperatorShiftReduceStackItem(lookAhead.value)
                }
            }

            parseStack.push(stackItem)

            //Reduce
            var canReduce = true
            while(canReduce) {
                val top = parseStack.pop()
                if (top is NodeShiftReduceStackItem) {
                    val node = top.node
                    if (parseStack.isNotEmpty()) {
                        val operatorItem = parseStack.pop() as OperatorShiftReduceStackItem
                        if (operatorItem.operator == TokenizerConstants.MINUS_OPERATOR) {
                            val resultNode = ParsedUnaryExpressionNode(node, operatorItem.operator)
                            parseStack.push(NodeShiftReduceStackItem(resultNode))
                        }
                        else if (operatorItem.operator == TokenizerConstants.DIVIDE_OPERATOR) {
                            val leftItem = parseStack.pop() as NodeShiftReduceStackItem
                            val resultNode = ParsedBinaryOperatorExpressionNode(leftItem.node, node, operatorItem.operator)
                            parseStack.push(NodeShiftReduceStackItem(resultNode))
                        }
                        else if (operatorItem.operator == TokenizerConstants.LESS_THAN_OPERATOR) {
                            val leftItem = parseStack.pop() as NodeShiftReduceStackItem
                            val resultNode = ParsedBinaryRelationalOperatorExpressionNode(leftItem.node, node, operatorItem.operator)
                            parseStack.push(NodeShiftReduceStackItem(resultNode))
                        }
                        else {
                            parseStack.push(operatorItem)
                            parseStack.push(top)
                            canReduce = false
                        }
                    }
                    else {
                        parseStack.push(top)
                        canReduce = false
                    }
                } else {
                    top as OperatorShiftReduceStackItem
                    val operator = top.operator

                    if (operator == TokenizerConstants.RIGHT_PARENTHESES) {
                        --leftRight
                        hasNotSeenParentheses = false
                        val nodeItem = parseStack.pop() as NodeShiftReduceStackItem
                        parseStack.pop() //LEFT_PARENTHESES
                        parseStack.push(NodeShiftReduceStackItem(ParsedInnerExpressionNode(nodeItem.node)))
                    }
                    else if (operator == TokenizerConstants.LEFT_PARENTHESES) {
                        ++leftRight
                        hasNotSeenParentheses = false
                        parseStack.push(top)
                        canReduce = false
                    }
                    else {
                        parseStack.push(top)
                        canReduce = false
                    }
                }
            }

            //Update look ahead
            ++currentPosition
            lookAhead = tokens[currentPosition]
        } while (acceptedTokenTypes.contains(lookAhead.type) && (hasNotSeenParentheses || leftRight > 0))

        val resultStackItem = parseStack.pop() as NodeShiftReduceStackItem
        return Pair(resultStackItem.node, currentPosition)
    }
}