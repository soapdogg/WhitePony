package compiler.translator.impl

import compiler.core.nodes.parsed.ParsedBinaryArrayExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryAssignExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IBinaryAssignArrayLValueExpressionTranslator
import compiler.translator.impl.internal.IBinaryAssignExpressionTranslator
import compiler.translator.impl.internal.IBinaryAssignVariableLValueExpressionTranslator

internal class BinaryAssignExpressionTranslator(
    private val binaryAssignVariableLValueExpressionTranslator: IBinaryAssignVariableLValueExpressionTranslator,
    private val binaryAssignArrayLValueExpressionTranslator: IBinaryAssignArrayLValueExpressionTranslator
): IBinaryAssignExpressionTranslator {
    override fun translate(
        node: ParsedBinaryAssignExpressionNode,
        location: Int,
        variableToTypeMap: Map<String, String>,
        stack: Stack<ExpressionTranslatorStackItem>,
        resultStack: Stack<TranslatedExpressionNode>
    ) {
        if (node.leftExpression is ParsedVariableExpressionNode) {
            binaryAssignVariableLValueExpressionTranslator.translate(
                node,
                node.leftExpression,
                location,
                variableToTypeMap,
                stack,
                resultStack
            )
        }
        else {
            binaryAssignArrayLValueExpressionTranslator.translate(
                node,
                node.leftExpression as ParsedBinaryArrayExpressionNode,
                location,
                variableToTypeMap,
                stack,
                resultStack
            )
        }
    }
}