package compiler.translator.impl.internal

import compiler.core.nodes.parsed.ParsedUnaryExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack

internal interface IUnaryExpressionTranslator {
    fun translate(
        node: ParsedUnaryExpressionNode,
        location: Int,
        tempCounter: Int,
        stack: Stack<ExpressionTranslatorStackItem>,
        resultStack: Stack<TranslatedExpressionNode>
    ): Int
}