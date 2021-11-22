package compiler.translator.impl.internal

import compiler.core.IParsedExpressionNode
import compiler.core.ITranslatedExpressionNode

internal interface IExpressionTranslator {
    fun translate(
        expressionNode: IParsedExpressionNode,
        labelCounter: Int,
        tempCounter: Int,
    ): ITranslatedExpressionNode
}