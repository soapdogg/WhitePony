package compiler.translator.impl

import compiler.core.IParsedExpressionNode
import compiler.core.TranslatedBooleanExpressionNode
import compiler.translator.impl.internal.IBooleanExpressionTranslator

internal class BooleanExpressionTranslator: IBooleanExpressionTranslator {
    override fun translate(
        expressionNode: IParsedExpressionNode,
        labelCounter: Int,
        tempCounter: Int
    ): Triple<TranslatedBooleanExpressionNode, Int, Int> {

        return Triple(TranslatedBooleanExpressionNode(expressionNode.toString()), labelCounter, tempCounter)
    }
}