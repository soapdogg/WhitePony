package compiler.translator.impl

import compiler.core.IParsedExpressionNode
import compiler.core.ITranslatedExpressionNode
import compiler.translator.impl.internal.IExpressionTranslator

internal class ExpressionTranslator: IExpressionTranslator {
    override fun translate(
        expressionNode: IParsedExpressionNode,
        labelCounter: Int,
        tempCounter: Int
    ): Triple<ITranslatedExpressionNode, Int, Int> {
        TODO("Not yet implemented")
    }
}