package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedInnerExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IExpressionTranslator

internal class InnerExpressionTranslator: IExpressionTranslator {
    override fun translate(
        node: IParsedExpressionNode,
        location: ExpressionTranslatorLocation,
        variableToTypeMap: Map<String, String>,
        tempCounter: Int,
        stack: Stack<ExpressionTranslatorStackItem>,
        resultStack: Stack<TranslatedExpressionNode>
    ): Int {
        node as ParsedInnerExpressionNode
        stack.push(ExpressionTranslatorStackItem(ExpressionTranslatorLocation.START, node.expression))
        return tempCounter
    }
}