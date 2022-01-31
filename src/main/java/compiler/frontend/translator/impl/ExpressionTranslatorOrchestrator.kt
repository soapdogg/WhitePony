package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.*
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.frontend.translator.impl.internal.*
import compiler.frontend.translator.impl.internal.IExpressionTranslatorOrchestrator

internal class ExpressionTranslatorOrchestrator(
    private val stackGenerator: IStackGenerator,
    private val translatorMap: Map<Class<out IParsedExpressionNode>, IExpressionTranslator>
): IExpressionTranslatorOrchestrator {
    override fun translate(
        expressionNode: IParsedExpressionNode,
        variableToTypeMap: Map<String, String>,
        tempCounter: Int
    ): Pair<TranslatedExpressionNode, Int> {
        val stack = stackGenerator.generateNewStack(ExpressionTranslatorStackItem::class.java)
        stack.push(ExpressionTranslatorStackItem(ExpressionTranslatorLocation.START, expressionNode))
        val resultStack = stackGenerator.generateNewStack(TranslatedExpressionNode::class.java)
        var localTemp = tempCounter

        while(stack.isNotEmpty()) {
            val top = stack.pop()

            val translator = translatorMap.getValue(top.node.javaClass)
            localTemp = translator.translate(
                top.node,
                top.location,
                variableToTypeMap,
                localTemp,
                stack,
                resultStack
            )
        }
        return Pair(resultStack.pop(), localTemp)
    }
}