package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedInnerExpressionNode
import compiler.core.nodes.translated.TranslatedBooleanExpressionNode
import compiler.core.stack.BooleanExpressionTranslatorStackItem
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IBooleanExpressionNodeTranslator
import compiler.translator.impl.internal.IBooleanExpressionTranslatorStackPusher

internal class InnerBooleanExpressionTranslator(
    private val booleanExpressionTranslatorStackPusher: IBooleanExpressionTranslatorStackPusher
): IBooleanExpressionNodeTranslator {
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
        node as ParsedInnerExpressionNode
        booleanExpressionTranslatorStackPusher.push(LocationConstants.LOCATION_1, node.expression, trueLabel, falseLabel,stack)
        return Pair(labelCounter, tempCounter)
    }
}