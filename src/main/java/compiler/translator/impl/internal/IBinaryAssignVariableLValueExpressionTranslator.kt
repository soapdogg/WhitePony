package compiler.translator.impl.internal

import compiler.core.nodes.parsed.ParsedBinaryAssignExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack

internal interface IBinaryAssignVariableLValueExpressionTranslator {
    fun translate(
        node: ParsedBinaryAssignExpressionNode,
        lValueNode: ParsedVariableExpressionNode,
        location: ExpressionTranslatorLocation,
        variableToTypeMap: Map<String, String>,
        stack: Stack<ExpressionTranslatorStackItem>,
        resultStack: Stack<TranslatedExpressionNode>
    )
}