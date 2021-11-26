package compiler.translator.impl

import compiler.core.nodes.parsed.ParsedConstantExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IConstantExpressionTranslator

internal class ConstantExpressionTranslator: IConstantExpressionTranslator {
    override fun translate(
        node: ParsedConstantExpressionNode,
        resultStack: Stack<TranslatedExpressionNode>
    ) {
        val translatedConstantNode = TranslatedExpressionNode(
            node.value,
            listOf(),
            node.type
        )
        resultStack.push(translatedConstantNode)
    }
}