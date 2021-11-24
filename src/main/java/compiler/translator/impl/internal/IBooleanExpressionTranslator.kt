package compiler.translator.impl.internal

import compiler.core.IParsedExpressionNode
import compiler.core.TranslatedBooleanExpressionNode

interface IBooleanExpressionTranslator {
    fun translate(
        expressionNode: IParsedExpressionNode,
        labelCounter: Int,
        tempCounter: Int,
    ): Triple<TranslatedBooleanExpressionNode, Int, Int>
}