package compiler.translator.impl

import compiler.core.nodes.parsed.*
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.translator.impl.internal.*
import compiler.translator.impl.internal.IExpressionTranslatorOrchestrator

internal class ExpressionTranslatorOrchestrator(
    private val translatorMap: Map<Class<out IParsedExpressionNode>, IExpressionTranslator>
): IExpressionTranslatorOrchestrator {
    override fun translate(
        expressionNode: IParsedExpressionNode,
        variableToTypeMap: Map<String, String>,
        tempCounter: Int
    ): Pair<TranslatedExpressionNode, Int> {
        val stack = Stack<ExpressionTranslatorStackItem>()
        stack.push(ExpressionTranslatorStackItem(ExpressionTranslatorLocation.START, expressionNode))
        val resultStack = Stack<TranslatedExpressionNode>()
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