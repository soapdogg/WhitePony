package compiler.translator.impl.internal

import compiler.core.nodes.parsed.ParsedBinaryOperatorExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack

internal interface IBinaryOperatorExpressionTranslator {
    fun translate(
        node: ParsedBinaryOperatorExpressionNode,
        location: ExpressionTranslatorLocation,
        tempCounter: Int,
        variableToTypeMap: Map<String, String>,
        stack: Stack<ExpressionTranslatorStackItem>,
        resultStack: Stack<TranslatedExpressionNode>
    ): Int
}