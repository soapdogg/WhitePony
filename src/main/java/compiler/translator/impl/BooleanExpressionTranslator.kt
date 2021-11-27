package compiler.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.parsed.*
import compiler.core.nodes.translated.TranslatedBooleanExpressionNode
import compiler.core.stack.BooleanExpressionTranslatorStackItem
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IBinaryAndOperatorExpressionTranslator
import compiler.translator.impl.internal.IBinaryOrOperatorExpressionTranslator
import compiler.translator.impl.internal.IBinaryRelationalOperatorExpressionTranslator
import compiler.translator.impl.internal.IBooleanExpressionTranslator

internal class BooleanExpressionTranslator(
    private val binaryAndOperatorExpressionTranslator: IBinaryAndOperatorExpressionTranslator,
    private val binaryOrOperatorExpressionTranslator: IBinaryOrOperatorExpressionTranslator,
    private val binaryRelationalOperatorExpressionTranslator: IBinaryRelationalOperatorExpressionTranslator
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
                is ParsedBinaryAndOperatorExpressionNode -> {
                    l = binaryAndOperatorExpressionTranslator.translate(
                        top.node,
                        top.location,
                        top.trueLabel,
                        top.falseLabel,
                        t,
                        l,
                        variableToTypeMap,
                        stack,
                        resultStack,
                        labelStack
                    )
                }
                is ParsedBinaryOrOperatorExpressionNode -> {
                    l = binaryOrOperatorExpressionTranslator.translate(
                        top.node,
                        top.location,
                        top.trueLabel,
                        top.falseLabel,
                        t,
                        l,
                        variableToTypeMap,
                        stack,
                        resultStack,
                        labelStack
                    )
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