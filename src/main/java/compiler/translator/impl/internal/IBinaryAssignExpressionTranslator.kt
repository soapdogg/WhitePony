package compiler.translator.impl.internal

import compiler.core.nodes.parsed.ParsedBinaryAssignExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack

internal interface IBinaryAssignExpressionTranslator {
    fun translate(
        node: ParsedBinaryAssignExpressionNode,
        location: ExpressionTranslatorLocation,
        variableToTypeMap: Map<String, String>,
        stack: Stack<ExpressionTranslatorStackItem>,
        resultStack: Stack<TranslatedExpressionNode>
    )
}