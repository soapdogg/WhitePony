package compiler.translator.impl

import compiler.core.ParsedArrayNode
import compiler.core.TranslatedArrayNode
import compiler.translator.impl.internal.IArrayTranslator
import compiler.translator.impl.internal.IExpressionTranslator

internal class ArrayTranslator(
    private val expressionTranslator: IExpressionTranslator
): IArrayTranslator {
    override fun translate(arrayNode: ParsedArrayNode): TranslatedArrayNode {
        val translatedExpressionNode = if (arrayNode.indexExpressionNode != null) {
            expressionTranslator.translate(arrayNode.indexExpressionNode)
        } else {
            null
        }

        return TranslatedArrayNode(translatedExpressionNode)
    }
}