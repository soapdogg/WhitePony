package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.*
import compiler.core.nodes.translated.TranslatedBooleanExpressionNode
import compiler.core.stack.BooleanExpressionTranslatorStackItem
import compiler.core.stack.LocationConstants
import compiler.frontend.translator.impl.internal.IBooleanExpressionTranslator
import compiler.frontend.translator.impl.internal.IBooleanExpressionTranslatorOrchestrator
import compiler.frontend.translator.impl.internal.IStackGenerator

internal class BooleanExpressionTranslatorOrchestrator(
    private val stackGenerator: IStackGenerator,
    private val translatorMap: Map<Class<out IParsedExpressionNode>, IBooleanExpressionTranslator>
): IBooleanExpressionTranslatorOrchestrator {
    override fun translate(
        expressionNode: IParsedExpressionNode,
        topTrueLabel: String,
        topFalseLabel: String,
        labelCounter: Int,
        tempCounter: Int,
        variableToTypeMap: Map<String, String>
    ): Triple<TranslatedBooleanExpressionNode, Int, Int> {

        val stack = stackGenerator.generateNewStack(BooleanExpressionTranslatorStackItem::class.java)
        stack.push(BooleanExpressionTranslatorStackItem(LocationConstants.LOCATION_1, expressionNode, topTrueLabel, topFalseLabel))
        val resultStack = stackGenerator.generateNewStack(TranslatedBooleanExpressionNode::class.java)
        val labelStack = stackGenerator.generateNewStack(String::class.java)
        var l = labelCounter
        var t = tempCounter

        while (stack.isNotEmpty()) {
            val top = stack.pop()

            val translator = translatorMap.getValue(top.node.javaClass)
            val translateResult = translator.translate(
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
            l = translateResult.first
            t = translateResult.second
        }

        return Triple(resultStack.pop(), l, t)
    }
}