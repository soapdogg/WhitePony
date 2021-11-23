package compiler.translator.impl

import compiler.core.*
import compiler.core.constants.PrinterConstants
import compiler.core.constants.TokenizerConstants
import compiler.translator.impl.internal.IExpressionTranslator
import kotlin.math.exp

internal class ExpressionTranslator: IExpressionTranslator {
    override fun translate(
        expressionNode: IParsedExpressionNode,
        l: Int,
        t: Int
    ): Triple<TranslatedExpressionNode, Int, Int> {
        val stack = Stack<ExpressionTranslatorStackItem>()
        stack.push(ExpressionTranslatorStackItem(1, expressionNode))
        val resultStack = Stack<TranslatedExpressionNode>()
        var labelCounter = l
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
                    else {
                        resultStack.push(TranslatedExpressionNode("", listOf(top.node.toString())))
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
        return Triple(resultStack.pop(), labelCounter, tempCounter)
    }
}