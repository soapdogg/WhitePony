package compiler.translator.impl.internal

import compiler.core.nodes.parsed.ParsedInnerExpressionNode
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack

internal interface IInnerExpressionTranslator {
    fun translate(
        node: ParsedInnerExpressionNode,
        stack: Stack<ExpressionTranslatorStackItem>
    )
}