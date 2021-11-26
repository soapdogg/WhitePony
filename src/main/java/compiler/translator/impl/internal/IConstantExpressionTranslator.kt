package compiler.translator.impl.internal

import compiler.core.nodes.parsed.ParsedConstantExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.Stack

internal interface IConstantExpressionTranslator {
    fun translate(
        node: ParsedConstantExpressionNode,
        resultStack: Stack<TranslatedExpressionNode>
    )
}