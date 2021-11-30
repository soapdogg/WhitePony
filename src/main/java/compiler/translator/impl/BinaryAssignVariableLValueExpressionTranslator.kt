package compiler.translator.impl

import compiler.core.nodes.parsed.ParsedBinaryAssignExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IAssignCodeGenerator
import compiler.translator.impl.internal.IBinaryAssignVariableLValueExpressionTranslator
import compiler.translator.impl.internal.IExpressionTranslatorStackPusher

internal class BinaryAssignVariableLValueExpressionTranslator(
    private val expressionTranslatorStackPusher: IExpressionTranslatorStackPusher,
    private val assignCodeGenerator: IAssignCodeGenerator
): IBinaryAssignVariableLValueExpressionTranslator {
    override fun translate(
        node: ParsedBinaryAssignExpressionNode,
        lValueNode: ParsedVariableExpressionNode,
        location: ExpressionTranslatorLocation,
        variableToTypeMap: Map<String, String>,
        stack: Stack<ExpressionTranslatorStackItem>,
        resultStack: Stack<TranslatedExpressionNode>
    ) {
        when (location) {
            ExpressionTranslatorLocation.START -> {
                expressionTranslatorStackPusher.push(
                    ExpressionTranslatorLocation.END,
                    node,
                    node.rightExpression,
                    stack
                )
            }
            else -> {
                val rightExpression = resultStack.pop()
                val address = rightExpression.address
                val type = variableToTypeMap.getValue(lValueNode.value)
                val assignCode = assignCodeGenerator.generateAssignCode(lValueNode.value, address)
                val code = rightExpression.code + listOf(assignCode)
                val translatedBinaryOperatorNode = TranslatedExpressionNode(
                    address,
                    code,
                    type
                )
                resultStack.push(translatedBinaryOperatorNode)
            }
        }
    }
}