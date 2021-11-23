package compiler.translator.impl.internal

import compiler.core.IParsedExpressionNode
import compiler.core.ITranslatedExpressionNode
import compiler.core.TranslatedExpressionNode

internal interface IExpressionTranslator {
    fun translate(
        expressionNode: IParsedExpressionNode,
        labelCounter: Int,
        tempCounter: Int,
    ): Triple<TranslatedExpressionNode, Int, Int>
}