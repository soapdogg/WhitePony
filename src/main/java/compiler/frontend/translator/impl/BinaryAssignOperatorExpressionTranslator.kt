package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryAssignOperatorExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.frontend.translator.impl.internal.IExpressionTranslator

internal class BinaryAssignOperatorExpressionTranslator(
    private val binaryAssignOperatorVariableLValueExpressionTranslator: IExpressionTranslator,
    private val binaryAssignOperatorArrayLValueExpressionTranslator: IExpressionTranslator
): IExpressionTranslator {
    override fun translate(
        node: IParsedExpressionNode,
        location: ExpressionTranslatorLocation,
        variableToTypeMap: Map<String, String>,
        tempCounter: Int,
        stack: Stack<ExpressionTranslatorStackItem>,
        resultStack: Stack<TranslatedExpressionNode>
    ): Int {
        node as ParsedBinaryAssignOperatorExpressionNode
        return if (node.leftExpression is ParsedVariableExpressionNode) {
            binaryAssignOperatorVariableLValueExpressionTranslator.translate(
                node,
                location,
                variableToTypeMap,
                tempCounter,
                stack,
                resultStack
            )
        } else {
            binaryAssignOperatorArrayLValueExpressionTranslator.translate(
                node,
                location,
                variableToTypeMap,
                tempCounter,
                stack,
                resultStack
            )
        }
    }
}