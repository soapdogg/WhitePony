package compiler.translator.impl

import compiler.core.*
import compiler.core.constants.PrinterConstants
import compiler.translator.impl.internal.IBooleanExpressionTranslator
import compiler.translator.impl.internal.IExpressionTranslator

internal class BooleanExpressionTranslator(
    private val expressionTranslator: IExpressionTranslator
): IBooleanExpressionTranslator {
    override fun translate(
        expressionNode: IParsedExpressionNode,
        trueLabel: String,
        falseLabel: String,
        labelCounter: Int,
        tempCounter: Int,
        variableToTypeMap: Map<String, String>
    ): Triple<TranslatedBooleanExpressionNode, Int, Int> {

        val stack = Stack<BooleanExpressionTranslatorStackItem>()
        stack.push(BooleanExpressionTranslatorStackItem(1, expressionNode, trueLabel, falseLabel))
        val resultStack = Stack<TranslatedBooleanExpressionNode>()
        val labelStack = Stack<String>()
        var l = labelCounter
        var t = tempCounter

        while (stack.isNotEmpty()) {
            val top = stack.pop()

            when(top.node) {
                is ParsedBinaryOrOperatorNode -> {
                    when(top.location) {
                        1 -> {
                            stack.push(BooleanExpressionTranslatorStackItem(2, top.node, top.trueLabel, top.falseLabel))
                            val falseLabel = "_l" + l
                            l++
                            labelStack.push(falseLabel)
                            stack.push(BooleanExpressionTranslatorStackItem(1, top.node.leftExpression, top.trueLabel, falseLabel))
                        }
                        2 -> {
                            stack.push(BooleanExpressionTranslatorStackItem(3, top.node, top.trueLabel, top.falseLabel))
                            stack.push(BooleanExpressionTranslatorStackItem(1, top.node.rightExpression, top.trueLabel, top.falseLabel))
                        }
                        3 -> {
                            val rightExpression = resultStack.pop()
                            val leftExpression = resultStack.pop()
                            val falseLabel = labelStack.pop()
                            val code = leftExpression.code +
                                    listOf(
                                        falseLabel + PrinterConstants.COLON + PrinterConstants.SPACE
                                    ) +
                                    rightExpression.code

                            val translatedBooleanExpressionNode = TranslatedBooleanExpressionNode(code)
                            resultStack.push(translatedBooleanExpressionNode)
                        }
                    }
                }
                is ParsedInnerExpression -> {
                    stack.push(BooleanExpressionTranslatorStackItem(1, top.node.expression, trueLabel, falseLabel))
                }
                is ParsedBinaryRelationalOperatorNode -> {
                    val (leftExpression, tempAfterLeft) = expressionTranslator.translate(top.node.leftExpression, variableToTypeMap, t)
                    val (rightExpression, tempAfterRight) = expressionTranslator.translate(top.node.rightExpression, variableToTypeMap, tempAfterLeft)
                    t = tempAfterRight

                    val code = leftExpression.code + rightExpression.code +
                            listOf(
                                PrinterConstants.IF +
                                        PrinterConstants.SPACE +
                                        PrinterConstants.LEFT_PARENTHESES +
                                        leftExpression.address +
                                        PrinterConstants.SPACE +
                                        top.node.operator +
                                        PrinterConstants.SPACE +
                                        rightExpression.address +
                                        PrinterConstants.RIGHT_PARENTHESES +
                                        PrinterConstants.SPACE +
                                        "goto" +
                                        PrinterConstants.SPACE +
                                        top.trueLabel,
                                "goto" +
                                        PrinterConstants.SPACE +
                                        top.falseLabel
                            )
                    val translatedBooleanExpressionNode = TranslatedBooleanExpressionNode(code)
                    resultStack.push(translatedBooleanExpressionNode)
                }
                else -> {
                    val translatedBooleanExpressionNode = TranslatedBooleanExpressionNode(listOf(expressionNode.toString()))
                    resultStack.push(translatedBooleanExpressionNode)
                }
            }
        }

        return Triple(resultStack.pop(), l, t)
    }
}