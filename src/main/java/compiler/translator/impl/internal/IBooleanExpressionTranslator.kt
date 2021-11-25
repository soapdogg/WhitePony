package compiler.translator.impl.internal

import compiler.core.IParsedExpressionNode
import compiler.core.TranslatedBooleanExpressionNode

interface IBooleanExpressionTranslator {
    fun translate(
        expressionNode: IParsedExpressionNode,
        trueLabel: String,
        falseLabel: String,
        labelCounter: Int,
        tempCounter: Int,
        variableToTypeMap: Map<String, String>
    ): Triple<TranslatedBooleanExpressionNode, Int, Int>
}