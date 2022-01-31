package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryAndOperatorExpressionNode
import compiler.core.nodes.translated.TranslatedBooleanExpressionNode
import compiler.core.stack.BooleanExpressionTranslatorStackItem
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import compiler.frontend.translator.impl.internal.IBooleanExpressionTranslator
import compiler.frontend.translator.impl.internal.IBooleanExpressionTranslatorStackPusher
import compiler.frontend.translator.impl.internal.ILabelCodeGenerator
import compiler.frontend.translator.impl.internal.ILabelGenerator

internal class BinaryAndOperatorExpressionTranslator(
    private val labelGenerator: ILabelGenerator,
    private val booleanExpressionTranslatorStackPusher: IBooleanExpressionTranslatorStackPusher,
    private val labelCodeGenerator: ILabelCodeGenerator
): IBooleanExpressionTranslator {
    override fun translate(
        node: IParsedExpressionNode,
        location: Int,
        trueLabel: String,
        falseLabel: String,
        tempCounter: Int,
        labelCounter: Int,
        variableToTypeMap: Map<String, String>,
        stack: Stack<BooleanExpressionTranslatorStackItem>,
        resultStack: Stack<TranslatedBooleanExpressionNode>,
        labelStack: Stack<String>
    ): Pair<Int, Int> {
        node as ParsedBinaryAndOperatorExpressionNode
        return when(location) {
            LocationConstants.LOCATION_1 -> {
                val (tLabel, l) = labelGenerator.generateLabel(labelCounter)
                labelStack.push(tLabel)
                booleanExpressionTranslatorStackPusher.push(LocationConstants.LOCATION_2, node, trueLabel, falseLabel, stack)
                booleanExpressionTranslatorStackPusher.push(LocationConstants.LOCATION_1, node.leftExpression, tLabel, falseLabel, stack)
                Pair(l, tempCounter)
            }
            LocationConstants.LOCATION_2 -> {
                booleanExpressionTranslatorStackPusher.push(LocationConstants.LOCATION_3, node, trueLabel, falseLabel, stack)
                booleanExpressionTranslatorStackPusher.push(LocationConstants.LOCATION_1, node.rightExpression, trueLabel, falseLabel, stack)
                Pair(labelCounter, tempCounter)
            }
            LocationConstants.LOCATION_3 -> {
                val rightExpression = resultStack.pop()
                val leftExpression = resultStack.pop()
                val tLabel = labelStack.pop()
                val labelCode = labelCodeGenerator.generateLabelCode(tLabel)
                val code = leftExpression.code + listOf(labelCode) + rightExpression.code
                val translatedBooleanExpressionNode = TranslatedBooleanExpressionNode(code)
                resultStack.push(translatedBooleanExpressionNode)
                Pair(labelCounter, tempCounter)
            }
            else -> {
                Pair(labelCounter, tempCounter)
            }
        }
    }
}