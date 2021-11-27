package compiler.translator.impl.internal

import compiler.core.nodes.parsed.ParsedBinaryArrayExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryAssignExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack

internal interface IBinaryAssignArrayLValueExpressionTranslator {
    fun translate(
        node: ParsedBinaryAssignExpressionNode,
        lValueNode: ParsedBinaryArrayExpressionNode,
        location: Int,
        variableToTypeMap: Map<String, String>,
        stack: Stack<ExpressionTranslatorStackItem>,
        resultStack: Stack<TranslatedExpressionNode>
    )
}