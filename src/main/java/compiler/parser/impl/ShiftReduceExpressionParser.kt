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
        var leftRightParentheses = 0
        var hasNotSeenBrackets = true
        var leftRightBracket = 0

        top@ do {
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

            //Update look ahead
            ++currentPosition
            lookAhead = tokens[currentPosition]

            //Reduce
            var canReduce = true
            while(canReduce) {
                val top = parseStack.pop()
                if (top is NodeShiftReduceStackItem) {
                    val node = top.node
                    if (parseStack.isNotEmpty()) {
                        val operatorItem = parseStack.pop() as OperatorShiftReduceStackItem
                        when (operatorItem.operator) {
                            TokenizerConstants.INCREMENT -> {
                                val resultNode = ParsedUnaryPreOperatorExpressionNode(node, TokenizerConstants.PLUS_OPERATOR)
                                parseStack.push(NodeShiftReduceStackItem(resultNode))
                            }
                            TokenizerConstants.NEGATION -> {
                                val resultNode = ParsedUnaryNotOperatorExpressionNode(node)
                                parseStack.push(NodeShiftReduceStackItem(resultNode))
                            }
                            TokenizerConstants.BIT_NEGATION -> {
                                val resultNode = ParsedUnaryExpressionNode(node, operatorItem.operator)
                                parseStack.push(NodeShiftReduceStackItem(resultNode))
                            }
                            TokenizerConstants.MINUS_OPERATOR, TokenizerConstants.PLUS_OPERATOR -> {
                                if (parseStack.isNotEmpty() ) {
                                    val leftItem = parseStack.pop() as NodeShiftReduceStackItem
                                    if (lookAhead.value == TokenizerConstants.MULTIPLY_OPERATOR
                                        || lookAhead.value == TokenizerConstants.DIVIDE_OPERATOR
                                        || lookAhead.value == TokenizerConstants.MODULUS_OPERATOR
                                    ) { //TODO this is precedence
                                        parseStack.push(leftItem)
                                        parseStack.push(operatorItem)
                                        parseStack.push(top)
                                        canReduce = false
                                    } else {
                                        val resultItem = ParsedBinaryOperatorExpressionNode(leftItem.node, node, operatorItem.operator)
                                        parseStack.push(NodeShiftReduceStackItem(resultItem))
                                    }
                                } else {
                                    val resultNode = ParsedUnaryExpressionNode(node, operatorItem.operator)
                                    parseStack.push(NodeShiftReduceStackItem(resultNode))
                                }
                            }
                            TokenizerConstants.DIVIDE_OPERATOR, TokenizerConstants.MODULUS_OPERATOR, TokenizerConstants.MULTIPLY_OPERATOR -> {
                                val leftItem = parseStack.pop() as NodeShiftReduceStackItem
                                val resultNode = ParsedBinaryOperatorExpressionNode(leftItem.node, node, operatorItem.operator)
                                parseStack.push(NodeShiftReduceStackItem(resultNode))
                            }
                            TokenizerConstants.LESS_THAN_OPERATOR, TokenizerConstants.LESS_THAN_EQUALS_OPERATOR,
                            TokenizerConstants.GREATER_THAN_OPERATOR, TokenizerConstants.GREATER_THAN_EQUALS_OPERATOR,
                            TokenizerConstants.RELATIONAL_EQUALS, TokenizerConstants.RELATIONAL_NOT_EQUALS -> {
                                val leftItem = parseStack.pop() as NodeShiftReduceStackItem

                                val resultNode = ParsedBinaryRelationalOperatorExpressionNode(leftItem.node, node, operatorItem.operator)
                                parseStack.push(NodeShiftReduceStackItem(resultNode))
                            }
                            TokenizerConstants.BITWISE_AND_OPERATOR -> {
                                val leftItem = parseStack.pop() as NodeShiftReduceStackItem
                                if (
                                    lookAhead.value == TokenizerConstants.PLUS_OPERATOR
                                    || lookAhead.value == TokenizerConstants.GREATER_THAN_OPERATOR
                                ) { //TODO precedence
                                    parseStack.push(leftItem)
                                    parseStack.push(operatorItem)
                                    parseStack.push(top)
                                    canReduce = false
                                } else {
                                    val resultNode = ParsedBinaryOperatorExpressionNode(leftItem.node, node, operatorItem.operator)
                                    parseStack.push(NodeShiftReduceStackItem(resultNode))
                                }
                            }
                            TokenizerConstants.BITWISE_XOR_OPERATOR -> {
                                val leftItem = parseStack.pop() as NodeShiftReduceStackItem
                                if (
                                    lookAhead.value == TokenizerConstants.PLUS_OPERATOR
                                    || lookAhead.value == TokenizerConstants.GREATER_THAN_OPERATOR
                                ) { //TODO precedence
                                    parseStack.push(leftItem)
                                    parseStack.push(operatorItem)
                                    parseStack.push(top)
                                    canReduce = false
                                } else {
                                    val resultNode = ParsedBinaryOperatorExpressionNode(leftItem.node, node, operatorItem.operator)
                                    parseStack.push(NodeShiftReduceStackItem(resultNode))
                                }
                            }
                            TokenizerConstants.BITWISE_OR_OPERATOR -> {
                                val leftItem = parseStack.pop() as NodeShiftReduceStackItem
                                if (
                                    lookAhead.value == TokenizerConstants.PLUS_OPERATOR
                                    || lookAhead.value == TokenizerConstants.GREATER_THAN_OPERATOR
                                ) { //TODO precedence
                                    parseStack.push(leftItem)
                                    parseStack.push(operatorItem)
                                    parseStack.push(top)
                                    canReduce = false
                                } else {
                                    val resultNode = ParsedBinaryOperatorExpressionNode(leftItem.node, node, operatorItem.operator)
                                    parseStack.push(NodeShiftReduceStackItem(resultNode))
                                }
                            }
                            TokenizerConstants.AND_OPERATOR -> {
                                val leftItem = parseStack.pop() as NodeShiftReduceStackItem
                                if (lookAhead.value == TokenizerConstants.GREATER_THAN_EQUALS_OPERATOR
                                    || lookAhead.value == TokenizerConstants.GREATER_THAN_OPERATOR
                                    || lookAhead.value == TokenizerConstants.LESS_THAN_OPERATOR
                                    || lookAhead.value == TokenizerConstants.LESS_THAN_EQUALS_OPERATOR
                                    || lookAhead.value == TokenizerConstants.RELATIONAL_EQUALS
                                    || lookAhead.value == TokenizerConstants.RELATIONAL_NOT_EQUALS
                                ) { //TODO This needs to be precedence
                                    parseStack.push(leftItem)
                                    parseStack.push(operatorItem)
                                    parseStack.push(top)
                                    canReduce = false
                                } else {
                                    val resultNode = ParsedBinaryAndOperatorExpressionNode(leftItem.node, node)
                                    parseStack.push(NodeShiftReduceStackItem(resultNode))
                                }
                            }
                            TokenizerConstants.OR_OPERATOR -> {
                                val leftItem = parseStack.pop() as NodeShiftReduceStackItem
                                if (lookAhead.value == TokenizerConstants.LESS_THAN_OPERATOR
                                    || lookAhead.value == TokenizerConstants.MODULUS_OPERATOR
                                    || lookAhead.value == TokenizerConstants.RELATIONAL_EQUALS
                                ) { //TODO this is supposed to be precedence
                                    parseStack.push(leftItem)
                                    parseStack.push(operatorItem)
                                    parseStack.push(top)
                                    canReduce = false
                                } else {
                                    val resultNode = ParsedBinaryOrOperatorExpressionNode(leftItem.node, node)
                                    parseStack.push(NodeShiftReduceStackItem(resultNode))
                                }
                            }
                            TokenizerConstants.ASSIGN_OPERATOR -> {
                                val leftItem = parseStack.pop() as NodeShiftReduceStackItem
                                if (
                                    lookAhead.value == TokenizerConstants.PLUS_OPERATOR
                                    || lookAhead.value == TokenizerConstants.BITWISE_OR_OPERATOR
                                    || lookAhead.value == TokenizerConstants.BITWISE_XOR_OPERATOR
                                    || lookAhead.value == TokenizerConstants.BITWISE_AND_OPERATOR
                                ){
                                    parseStack.push(leftItem)
                                    parseStack.push(operatorItem)
                                    parseStack.push(top)
                                    canReduce = false
                                } else {
                                    val resultNode = ParsedBinaryAssignExpressionNode(leftItem.node, node)
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
                    else {
                        parseStack.push(top)
                        canReduce = false
                    }
                } else {
                    top as OperatorShiftReduceStackItem
                    val operator = top.operator

                    if (operator == TokenizerConstants.RIGHT_PARENTHESES) {
                        --leftRightParentheses
                        if (leftRightParentheses < 0) {
                            currentPosition--
                            break@top
                        }
                        hasNotSeenParentheses = false
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
                        hasNotSeenBrackets = false
                        val nodeItem = parseStack.pop() as NodeShiftReduceStackItem
                        parseStack.pop() //LEFT_BRACKET
                        val variableItem = parseStack.pop() as NodeShiftReduceStackItem
                        parseStack.push(NodeShiftReduceStackItem(ParsedBinaryArrayExpressionNode(variableItem.node as ParsedVariableExpressionNode, nodeItem.node)))
                    }
                    else if (operator == TokenizerConstants.LEFT_BRACKET) {
                        ++leftRightBracket
                        hasNotSeenBrackets = false
                        parseStack.push(top)
                        canReduce = false
                    }
                    else if (operator == TokenizerConstants.INCREMENT) {
                        if (parseStack.isNotEmpty()) {
                            val nodeItem = parseStack.pop() as NodeShiftReduceStackItem
                            val resultNode = ParsedUnaryPostOperatorExpressionNode(nodeItem.node, TokenizerConstants.PLUS_OPERATOR, TokenizerConstants.MINUS_OPERATOR)
                            parseStack.push(NodeShiftReduceStackItem(resultNode))
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


        } while (acceptedTokenTypes.contains(lookAhead.type) && (hasNotSeenParentheses || leftRightParentheses > 0) && (hasNotSeenBrackets || leftRightBracket > 0))

        val resultStackItem = parseStack.pop() as NodeShiftReduceStackItem
        return Pair(resultStackItem.node, currentPosition)
    }
}