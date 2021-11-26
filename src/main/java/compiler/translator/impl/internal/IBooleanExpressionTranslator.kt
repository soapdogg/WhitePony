package compiler.translator.impl.internal

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.translated.TranslatedBooleanExpressionNode

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