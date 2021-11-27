package compiler.translator.impl.internal

import compiler.core.nodes.parsed.ParsedBinaryRelationalOperatorExpressionNode
import compiler.core.nodes.translated.TranslatedBooleanExpressionNode
import compiler.core.stack.BooleanExpressionTranslatorStackItem
import compiler.core.stack.Stack

internal interface IBinaryRelationalOperatorExpressionTranslator {
    fun translate(
        node: ParsedBinaryRelationalOperatorExpressionNode,
        trueLabel: String,
        falseLabel: String,
        tempCounter: Int,
        variableToTypeMap: Map<String, String>,
        stack: Stack<BooleanExpressionTranslatorStackItem>,
        resultStack: Stack<TranslatedBooleanExpressionNode>
    ): Int
}