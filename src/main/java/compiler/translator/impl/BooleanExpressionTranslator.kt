package compiler.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.parsed.*
import compiler.core.nodes.translated.TranslatedBooleanExpressionNode
import compiler.core.stack.BooleanExpressionTranslatorStackItem
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IBinaryRelationalOperatorExpressionTranslator
import compiler.translator.impl.internal.IBooleanExpressionTranslator
import compiler.translator.impl.internal.IExpressionTranslator
import compiler.translator.impl.internal.IGotoCodeGenerator

internal class BooleanExpressionTranslator(
    private val binaryRelationalOperatorExpressionTranslator: IBinaryRelationalOperatorExpressionTranslator,
    private val expressionTranslator: IExpressionTranslator,
    private val gotoCodeGenerator: IGotoCodeGenerator
): IBooleanExpressionTranslator {
    override fun translate(
        expressionNode: IParsedExpressionNode,
        topTrueLabel: String,
        topFalseLabel: String,
        labelCounter: Int,
        tempCounter: Int,
        variableToTypeMap: Map<String, String>
    ): Triple<TranslatedBooleanExpressionNode, Int, Int> {

        val stack = Stack<BooleanExpressionTranslatorStackItem>()
        stack.push(BooleanExpressionTranslatorStackItem(LocationConstants.LOCATION_1, expressionNode, topTrueLabel, topFalseLabel))
        val resultStack = Stack<TranslatedBooleanExpressionNode>()
        val labelStack = Stack<String>()
        var l = labelCounter
        var t = tempCounter

        while (stack.isNotEmpty()) {
            val top = stack.pop()

            when(top.node) {
                is ParsedBinaryAndOperatorNode -> {
                    when(top.location) {
                        LocationConstants.LOCATION_1 -> {
                            stack.push(BooleanExpressionTranslatorStackItem(LocationConstants.LOCATION_2, top.node, top.trueLabel, top.falseLabel))
                            val trueLabel = "_l" + l
                            l++
                            labelStack.push(trueLabel)
                            stack.push(BooleanExpressionTranslatorStackItem(LocationConstants.LOCATION_1, top.node.leftExpression, trueLabel, top.falseLabel))
                        }
                        LocationConstants.LOCATION_2 -> {
                            stack.push(BooleanExpressionTranslatorStackItem(LocationConstants.LOCATION_3, top.node, top.trueLabel, top.falseLabel))
                            stack.push(BooleanExpressionTranslatorStackItem(LocationConstants.LOCATION_1, top.node.rightExpression, top.trueLabel, top.falseLabel))
                        }
                        LocationConstants.LOCATION_3 -> {
                            val rightExpression = resultStack.pop()
                            val leftExpression = resultStack.pop()
                            val trueLabel = labelStack.pop()
                            val code = leftExpression.code +
                                    listOf(
                                        trueLabel + PrinterConstants.COLON + PrinterConstants.SPACE
                                    ) +
                                    rightExpression.code
                            val translatedBooleanExpressionNode = TranslatedBooleanExpressionNode(code)
                            resultStack.push(translatedBooleanExpressionNode)
                        }
                    }
                }
                is ParsedBinaryOrOperatorNode -> {
                    when(top.location) {
                        LocationConstants.LOCATION_1 -> {
                            stack.push(BooleanExpressionTranslatorStackItem(LocationConstants.LOCATION_2, top.node, top.trueLabel, top.falseLabel))
                            val falseLabel = "_l" + l
                            l++
                            labelStack.push(falseLabel)
                            stack.push(BooleanExpressionTranslatorStackItem(LocationConstants.LOCATION_1, top.node.leftExpression, top.trueLabel, falseLabel))
                        }
                        LocationConstants.LOCATION_2 -> {
                            stack.push(BooleanExpressionTranslatorStackItem(LocationConstants.LOCATION_3, top.node, top.trueLabel, top.falseLabel))
                            stack.push(BooleanExpressionTranslatorStackItem(LocationConstants.LOCATION_1, top.node.rightExpression, top.trueLabel, top.falseLabel))
                        }
                        LocationConstants.LOCATION_3 -> {
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
                is ParsedUnaryNotOperatorNode -> {
                    stack.push(BooleanExpressionTranslatorStackItem(LocationConstants.LOCATION_1, top.node.expression, top.falseLabel, top.trueLabel))
                }
                is ParsedInnerExpressionNode -> {
                    stack.push(BooleanExpressionTranslatorStackItem(LocationConstants.LOCATION_1, top.node.expression, top.trueLabel, top.falseLabel))
                }
                is ParsedBinaryRelationalOperatorExpressionNode -> {
                    t = binaryRelationalOperatorExpressionTranslator.translate(
                        top.node,
                        top.trueLabel,
                        top.falseLabel,
                        t,
                        variableToTypeMap,
                        stack,
                        resultStack
                    )
                }
            }
        }

        return Triple(resultStack.pop(), l, t)
    }
}