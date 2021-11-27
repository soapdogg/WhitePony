package compiler.translator.impl

import compiler.core.nodes.parsed.ParsedBinaryAndOperatorExpressionNode
import compiler.core.nodes.translated.TranslatedBooleanExpressionNode
import compiler.core.stack.BooleanExpressionTranslatorStackItem
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IBinaryAndOperatorExpressionTranslator
import compiler.translator.impl.internal.IBooleanExpressionTranslatorStackPusher
import compiler.translator.impl.internal.ILabelCodeGenerator
import compiler.translator.impl.internal.ILabelGenerator

internal class BinaryAndOperatorExpressionTranslator(
    private val labelGenerator: ILabelGenerator,
    private val booleanExpressionTranslatorStackPusher: IBooleanExpressionTranslatorStackPusher,
    private val labelCodeGenerator: ILabelCodeGenerator
): IBinaryAndOperatorExpressionTranslator {
    override fun translate(
        node: ParsedBinaryAndOperatorExpressionNode,
        location: Int,
        trueLabel: String,
        falseLabel: String,
        tempCounter: Int,
        labelCounter: Int,
        variableToTypeMap: Map<String, String>,
        stack: Stack<BooleanExpressionTranslatorStackItem>,
        resultStack: Stack<TranslatedBooleanExpressionNode>,
        labelStack: Stack<String>
    ): Int {
        return when(location) {
            LocationConstants.LOCATION_1 -> {
                val (tLabel, l) = labelGenerator.generateLabel(labelCounter)
                labelStack.push(tLabel)
                booleanExpressionTranslatorStackPusher.push(LocationConstants.LOCATION_2, node, trueLabel, falseLabel, stack)
                booleanExpressionTranslatorStackPusher.push(LocationConstants.LOCATION_1, node.leftExpression, tLabel, falseLabel, stack)
                l
            }
            LocationConstants.LOCATION_2 -> {
                booleanExpressionTranslatorStackPusher.push(LocationConstants.LOCATION_3, node, trueLabel, falseLabel, stack)
                booleanExpressionTranslatorStackPusher.push(LocationConstants.LOCATION_1, node.rightExpression, trueLabel, falseLabel, stack)
                labelCounter
            }
            LocationConstants.LOCATION_3 -> {
                val rightExpression = resultStack.pop()
                val leftExpression = resultStack.pop()
                val tLabel = labelStack.pop()
                val labelCode = labelCodeGenerator.generateLabelCode(tLabel)
                val code = leftExpression.code + listOf(labelCode) + rightExpression.code
                val translatedBooleanExpressionNode = TranslatedBooleanExpressionNode(code)
                resultStack.push(translatedBooleanExpressionNode)
                labelCounter
            }
            else -> {
                labelCounter
            }
        }
    }
}