package compiler.translator.impl

import compiler.core.*
import compiler.core.constants.PrinterConstants
import compiler.core.constants.TokenizerConstants
import compiler.translator.impl.internal.IExpressionTranslator
import kotlin.math.exp

internal class ExpressionTranslator: IExpressionTranslator {
    override fun translate(
        expressionNode: IParsedExpressionNode,
        t: Int
    ): Pair<TranslatedExpressionNode, Int> {
        val stack = Stack<ExpressionTranslatorStackItem>()
        stack.push(ExpressionTranslatorStackItem(1, expressionNode))
        val resultStack = Stack<TranslatedExpressionNode>()
        var tempCounter = t

        while(stack.isNotEmpty()) {
            val top = stack.pop()

            when (top.node) {
                is ParsedBinaryAssignNode -> {
                    if (top.node.leftExpression is ParsedVariableExpressionNode) {
                        when (top.location) {
                            1 -> {
                                stack.push(ExpressionTranslatorStackItem(2, top.node))
                                stack.push(ExpressionTranslatorStackItem(1, top.node.rightExpression))
                            }
                            2 -> {
                                val rightExpression = resultStack.pop()
                                val address = rightExpression.address
                                val code = rightExpression.code + listOf(top.node.leftExpression.value + " = " + rightExpression.address)
                                val translatedBinaryOperatorNode = TranslatedExpressionNode(
                                    address,
                                    code
                                )
                                resultStack.push(translatedBinaryOperatorNode)
                            }
                        }
                    }
                    else if(top.node.leftExpression is ParsedBinaryArrayOperatorNode) {
                        when (top.location) {
                            1 -> {
                                stack.push(ExpressionTranslatorStackItem(2, top.node))
                                stack.push(ExpressionTranslatorStackItem(1, top.node.leftExpression.rightExpression))
                            }
                            2 -> {
                                stack.push(ExpressionTranslatorStackItem(3, top.node))
                                stack.push(ExpressionTranslatorStackItem(1, top.node.rightExpression))
                            }
                            3 -> {
                                val rightExpression = resultStack.pop()
                                val insideArrayExpression = resultStack.pop()
                                val code = insideArrayExpression.code +
                                        rightExpression.code +
                                        listOf(top.node.leftExpression.leftExpression.value +
                                                PrinterConstants.LEFT_BRACKET +
                                                insideArrayExpression.address +
                                                PrinterConstants.RIGHT_BRACKET +
                                                PrinterConstants.SPACE +
                                                PrinterConstants.EQUALS +
                                                PrinterConstants.SPACE +
                                                rightExpression.address
                                        )
                                val translatedExpressionNode = TranslatedExpressionNode(
                                    rightExpression.address,
                                    code,
                                )
                                resultStack.push(translatedExpressionNode)
                            }
                        }
                    }
                }
                is ParsedBinaryAssignOperatorNode -> {
                    if (top.node.leftExpression is ParsedVariableExpressionNode) {
                        when (top.location) {
                            1 -> {
                                stack.push(ExpressionTranslatorStackItem(2, top.node))
                                stack.push(ExpressionTranslatorStackItem(1, top.node.rightExpression))
                            }
                            2 -> {
                                val rightExpression = resultStack.pop()
                                val address = "_t" + tempCounter
                                tempCounter++
                                val code = rightExpression.code +
                                        listOf(
                                            address +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.EQUALS +
                                                    PrinterConstants.SPACE +
                                                    top.node.leftExpression.value +
                                                    PrinterConstants.SPACE +
                                                    top.node.operator +
                                                    PrinterConstants.SPACE +
                                                    rightExpression.address,
                                            top.node.leftExpression.value +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.EQUALS +
                                                    PrinterConstants.SPACE +
                                                    address
                                        )
                                val translatedExpressionNode = TranslatedExpressionNode(
                                    address,
                                    code
                                )
                                resultStack.push(translatedExpressionNode)
                            }
                        }
                    } else if (top.node.leftExpression is ParsedBinaryArrayOperatorNode){
                        when (top.location) {
                            1 -> {
                                stack.push(ExpressionTranslatorStackItem(2, top.node))
                                stack.push(ExpressionTranslatorStackItem(1, top.node.leftExpression.rightExpression))
                            }
                            2 -> {
                                stack.push(ExpressionTranslatorStackItem(3, top.node))
                                stack.push(ExpressionTranslatorStackItem(1, top.node.rightExpression))
                            }
                            3 -> {
                                val rightExpression = resultStack.pop()
                                val insideArrayExpression = resultStack.pop()
                                val address = "_t" + tempCounter
                                tempCounter++
                                val code = insideArrayExpression.code +
                                        listOf(address +
                                                PrinterConstants.SPACE +
                                                PrinterConstants.EQUALS +
                                                PrinterConstants.SPACE +
                                                top.node.leftExpression.leftExpression.value +
                                                PrinterConstants.LEFT_BRACKET +
                                                insideArrayExpression.address +
                                                PrinterConstants.RIGHT_BRACKET
                                        ) +
                                    rightExpression.code +
                                        listOf(address +
                                            PrinterConstants.SPACE +
                                                PrinterConstants.EQUALS +
                                                PrinterConstants.SPACE +
                                                address +
                                                PrinterConstants.SPACE +
                                                top.node.operator +
                                                PrinterConstants.SPACE +
                                                rightExpression.address,
                                            top.node.leftExpression.leftExpression.value +
                                                    PrinterConstants.LEFT_BRACKET +
                                                    insideArrayExpression.address +
                                                    PrinterConstants.RIGHT_BRACKET +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.EQUALS +
                                                    PrinterConstants.SPACE +
                                                    address
                                        )

                                val translatedExpressionNode = TranslatedExpressionNode(
                                    address,
                                    code
                                )
                                resultStack.push(translatedExpressionNode)
                            }
                        }
                    }
                }
                is ParsedBinaryOperatorNode -> {
                    when (top.location) {
                        1 -> {
                            stack.push(ExpressionTranslatorStackItem(2, top.node))
                            stack.push(ExpressionTranslatorStackItem(1, top.node.leftExpression))
                        }
                        2 -> {
                            stack.push(ExpressionTranslatorStackItem(3, top.node))
                            stack.push(ExpressionTranslatorStackItem(1, top.node.rightExpression))
                        }
                        3 -> {
                            val rightExpression = resultStack.pop()
                            val leftExpression = resultStack.pop()
                            val address = "_t" + tempCounter
                            tempCounter++
                            val code = leftExpression.code +
                                rightExpression.code +
                                listOf(address +
                                PrinterConstants.SPACE +
                                PrinterConstants.EQUALS +
                                PrinterConstants.SPACE +
                                leftExpression.address +
                                PrinterConstants.SPACE +
                                top.node.operator +
                                PrinterConstants.SPACE +
                                rightExpression.address)
                            val translatedBinaryOperatorNode = TranslatedExpressionNode(
                                address,
                                code
                            )
                            resultStack.push(translatedBinaryOperatorNode)
                        }
                    }
                }
                is ParsedBinaryArrayOperatorNode -> {
                    when(top.location) {
                        1 -> {
                            stack.push(ExpressionTranslatorStackItem(2, top.node))
                            stack.push(ExpressionTranslatorStackItem(1, top.node.rightExpression))
                        }
                        2 -> {
                            val rightExpression = resultStack.pop()
                            val address = "_t" + tempCounter
                            tempCounter++
                            val code = rightExpression.code +
                                    listOf(address + PrinterConstants.SPACE + PrinterConstants.EQUALS + PrinterConstants.SPACE + top.node.leftExpression.value + PrinterConstants.LEFT_BRACKET + rightExpression.address + PrinterConstants.RIGHT_BRACKET)
                            val translatedExpressionNode = TranslatedExpressionNode(
                                address,
                                code
                            )
                            resultStack.push(translatedExpressionNode)
                        }
                    }
                }
                is ParsedUnaryOperatorNode -> {
                    when (top.location) {
                        1 -> {
                            stack.push(ExpressionTranslatorStackItem(2, top.node))
                            stack.push(ExpressionTranslatorStackItem(1, top.node.expression))
                        }
                        2 -> {
                            val expression = resultStack.pop()
                            if (top.node.operator == TokenizerConstants.PLUS_OPERATOR) {
                                resultStack.push(expression)
                            } else {
                                val address ="_t" + tempCounter
                                tempCounter++
                                val code = expression.code +
                                        listOf(
                                            address +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.EQUALS +
                                                    PrinterConstants.SPACE +
                                                    top.node.operator +
                                                    expression.address
                                        )
                                val translatedExpressionNode = TranslatedExpressionNode(
                                    address,
                                    code
                                )
                                resultStack.push(translatedExpressionNode)
                            }
                        }
                    }
                }
                is ParsedUnaryPreOperatorNode -> {
                    if (top.node.expression is ParsedVariableExpressionNode) {
                        val code = listOf(
                            top.node.expression.value +
                                    PrinterConstants.SPACE +
                                    PrinterConstants.EQUALS +
                                    PrinterConstants.SPACE +
                                    top.node.expression.value +
                                    PrinterConstants.SPACE +
                                    top.node.operator +
                                    PrinterConstants.SPACE +
                                    PrinterConstants.ONE
                        )
                        val translatedExpressionNode = TranslatedExpressionNode(
                            top.node.expression.value,
                            code
                        )
                        resultStack.push(translatedExpressionNode)
                    } else if (top.node.expression is ParsedBinaryArrayOperatorNode) {
                        when (top.location) {
                            1 -> {
                                stack.push(ExpressionTranslatorStackItem(2, top.node))
                                stack.push(ExpressionTranslatorStackItem(1, top.node.expression.rightExpression))
                            }
                            2 -> {
                                val insideExpression = resultStack.pop()
                                val address = "_t" + tempCounter
                                tempCounter++
                                val code = insideExpression.code +
                                        listOf(
                                            address +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.EQUALS +
                                                    PrinterConstants.SPACE +
                                                    top.node.expression.leftExpression.value +
                                                    PrinterConstants.LEFT_BRACKET +
                                                    insideExpression.address +
                                                    PrinterConstants.RIGHT_BRACKET,
                                            address +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.EQUALS +
                                                    PrinterConstants.SPACE +
                                                    address +
                                                    PrinterConstants.SPACE +
                                                    top.node.operator +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.ONE,
                                            top.node.expression.leftExpression.value +
                                                    PrinterConstants.LEFT_BRACKET +
                                                    insideExpression.address +
                                                    PrinterConstants.RIGHT_BRACKET +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.EQUALS +
                                                    PrinterConstants.SPACE +
                                                    address
                                        )
                                val translatedExpressionNode = TranslatedExpressionNode(
                                    address,
                                    code
                                )
                                resultStack.push(translatedExpressionNode)
                            }
                        }
                    }
                }
                is ParsedUnaryPostOperatorNode -> {
                    if (top.node.expression is ParsedVariableExpressionNode) {
                        val address = "_t" + tempCounter
                        tempCounter++
                        val code = listOf(
                            address +
                                    PrinterConstants.SPACE +
                                    PrinterConstants.EQUALS +
                                    PrinterConstants.SPACE +
                                    top.node.expression.value,
                            top.node.expression.value +
                                    PrinterConstants.SPACE +
                                    PrinterConstants.EQUALS +
                                    PrinterConstants.SPACE +
                                    top.node.expression.value +
                                    PrinterConstants.SPACE +
                                    top.node.operator +
                                    PrinterConstants.SPACE +
                                    PrinterConstants.ONE
                        )
                        val translatedExpressionNode = TranslatedExpressionNode(
                            address,
                            code
                        )
                        resultStack.push(translatedExpressionNode)
                    } else if (top.node.expression is ParsedBinaryArrayOperatorNode) {
                        when (top.location) {
                            1 -> {
                                stack.push(ExpressionTranslatorStackItem(2, top.node))
                                stack.push(ExpressionTranslatorStackItem(1, top.node.expression.rightExpression))
                            }
                            2 -> {
                                val insideExpression = resultStack.pop()
                                val address = "_t" + tempCounter
                                tempCounter++
                                val code = insideExpression.code +
                                        listOf(
                                            address +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.EQUALS +
                                                    PrinterConstants.SPACE +
                                                    top.node.expression.leftExpression.value +
                                                    PrinterConstants.LEFT_BRACKET +
                                                    insideExpression.address +
                                                    PrinterConstants.RIGHT_BRACKET,
                                            address +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.EQUALS +
                                                    PrinterConstants.SPACE +
                                                    address +
                                                    PrinterConstants.SPACE +
                                                    top.node.operator +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.ONE,
                                            top.node.expression.leftExpression.value +
                                                    PrinterConstants.LEFT_BRACKET +
                                                    insideExpression.address +
                                                    PrinterConstants.RIGHT_BRACKET +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.EQUALS +
                                                    PrinterConstants.SPACE +
                                                    address,
                                            address +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.EQUALS +
                                                    PrinterConstants.SPACE +
                                                    address +
                                                    PrinterConstants.SPACE +
                                                    top.node.oppositeOperator +
                                                    PrinterConstants.SPACE +
                                                    PrinterConstants.ONE
                                        )
                                val translatedExpressionNode = TranslatedExpressionNode(
                                    address,
                                    code
                                )
                                resultStack.push(translatedExpressionNode)
                            }
                        }
                    }
                }
                is ParsedInnerExpression -> {
                    stack.push(ExpressionTranslatorStackItem(1, top.node.expression))
                }
                is ParsedVariableExpressionNode -> {
                    val address = "_t" + tempCounter
                    tempCounter++
                    val code = address + " = " + top.node.value
                    val translatedVariableExpressionNode = TranslatedExpressionNode(
                        address,
                        listOf(code)
                    )
                    resultStack.push(translatedVariableExpressionNode)
                }
                is ParsedConstantNode -> {
                    val translatedConstantNode = TranslatedExpressionNode(
                        top.node.value,
                        listOf()
                    )
                    resultStack.push(translatedConstantNode)
                }
                else -> {
                    resultStack.push(TranslatedExpressionNode("", listOf(top.node.toString())))
                }
            }
        }
        return Pair(resultStack.pop(), tempCounter)
    }
}