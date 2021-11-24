package compiler.translator.impl.internal

import compiler.core.IParsedExpressionNode
import compiler.core.ITranslatedExpressionNode
import compiler.core.TranslatedExpressionNode

internal interface IExpressionTranslator {
    fun translate(
        expressionNode: IParsedExpressionNode,
        variableToTypeMap: Map<String, String>,
        tempCounter: Int,
    ): Pair<TranslatedExpressionNode, Int>
}