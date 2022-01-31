package compiler.frontend.translator.impl.internal

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode

internal interface IExpressionTranslatorOrchestrator {
    fun translate(
        expressionNode: IParsedExpressionNode,
        variableToTypeMap: Map<String, String>,
        tempCounter: Int,
    ): Pair<TranslatedExpressionNode, Int>
}