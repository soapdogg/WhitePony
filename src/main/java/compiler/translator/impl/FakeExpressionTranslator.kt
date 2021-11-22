package compiler.translator.impl

import compiler.core.FakeTranslatedExpressionNode
import compiler.core.IParsedExpressionNode
import compiler.core.ITranslatedExpressionNode
import compiler.translator.impl.internal.IExpressionTranslator

class FakeExpressionTranslator: IExpressionTranslator {
    override fun translate(
        expressionNode: IParsedExpressionNode,
        labelCounter: Int,
        tempCounter: Int
    ): ITranslatedExpressionNode {
        return FakeTranslatedExpressionNode(expressionNode)
    }
}