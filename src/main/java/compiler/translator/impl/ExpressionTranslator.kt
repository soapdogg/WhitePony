package compiler.translator.impl

import compiler.core.*
import compiler.core.constants.PrinterConstants
import compiler.translator.impl.internal.IExpressionTranslator

internal class ExpressionTranslator: IExpressionTranslator {
    override fun translate(
        expressionNode: IParsedExpressionNode,
        l: Int,
        t: Int
    ): Triple<ITranslatedExpressionNode, Int, Int> {
        val stack = Stack<ExpressionTranslatorStackItem>()
        stack.push(ExpressionTranslatorStackItem(1, expressionNode))
        val resultStack = Stack<ITranslatedExpressionNode>()
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
                                val code = rightExpression.code + listOf(top.node.leftExpression.value + " = " + rightExpression.address + ";")
                                val translatedBinaryOperatorNode = TranslatedBinaryAssignNode(
                                    address,
                                    code
                                )
                                resultStack.push(translatedBinaryOperatorNode)
                            }
                        }
                    }
                    else {
                        resultStack.push(FakeTranslatedExpressionNode("", listOf(top.node.toString())))
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
                            val translatedBinaryOperatorNode = TranslatedBinaryOperatorNode(
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
                    val translatedVariableExpressionNode = TranslatedVariableExpressionNode(
                        address,
                        listOf(code)
                    )
                    resultStack.push(translatedVariableExpressionNode)
                }
                is ParsedConstantNode -> {
                    val translatedConstantNode = TranslatedConstantNode(
                        top.node.value,
                        listOf()
                    )
                    resultStack.push(translatedConstantNode)
                }
                else -> {
                    resultStack.push(FakeTranslatedExpressionNode("", listOf(top.node.toString())))
                }
            }
        }
        return Triple(resultStack.pop(), labelCounter, tempCounter)
    }
}