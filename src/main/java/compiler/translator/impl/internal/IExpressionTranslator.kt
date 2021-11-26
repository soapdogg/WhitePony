package compiler.translator.impl.internal

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode

internal interface IExpressionTranslator {
    fun translate(
        expressionNode: IParsedExpressionNode,
        variableToTypeMap: Map<String, String>,
        tempCounter: Int,
    ): Pair<TranslatedExpressionNode, Int>
}