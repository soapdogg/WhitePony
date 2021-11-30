package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryNotOperatorExpressionNode
import compiler.core.nodes.translated.TranslatedBooleanExpressionNode
import compiler.core.stack.BooleanExpressionTranslatorStackItem
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IBooleanExpressionTranslator
import compiler.translator.impl.internal.IBooleanExpressionTranslatorStackPusher

internal class UnaryNotExpressionTranslator(
    private val booleanExpressionTranslatorStackPusher: IBooleanExpressionTranslatorStackPusher
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
        node as ParsedUnaryNotOperatorExpressionNode
        booleanExpressionTranslatorStackPusher.push(LocationConstants.LOCATION_1, node.expression, falseLabel, trueLabel, stack)
        return Pair(labelCounter, tempCounter)
    }
}