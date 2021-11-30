package compiler.translator.impl.internal

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.translated.TranslatedBooleanExpressionNode
import compiler.core.stack.BooleanExpressionTranslatorStackItem
import compiler.core.stack.Stack

internal interface IBooleanExpressionTranslator {
    fun translate(
        node: IParsedExpressionNode,
        location: Int,
        trueLabel: String,
        falseLabel: String,
        tempCounter: Int,
        labelCounter: Int,
        variableToTypeMap: Map<String, String>,
        stack: Stack<BooleanExpressionTranslatorStackItem>,
        resultStack: Stack<TranslatedBooleanExpressionNode>,
        labelStack: Stack<String>
    ): Pair<Int, Int>
}