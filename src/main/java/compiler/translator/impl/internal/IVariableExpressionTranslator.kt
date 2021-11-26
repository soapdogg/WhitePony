package compiler.translator.impl.internal

import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.Stack

internal interface IVariableExpressionTranslator {
    fun translate(
        node: ParsedVariableExpressionNode,
        variableToTypeMap: Map<String, String>,
        tempCounter: Int,
        resultStack: Stack<TranslatedExpressionNode>
    ): Int
}