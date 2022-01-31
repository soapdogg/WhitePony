package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryAssignOperatorExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.frontend.translator.impl.internal.*
import compiler.frontend.translator.impl.internal.IAssignCodeGenerator
import compiler.frontend.translator.impl.internal.IExpressionTranslator
import compiler.frontend.translator.impl.internal.IExpressionTranslatorStackPusher
import compiler.frontend.translator.impl.internal.ITempDeclarationCodeGenerator
import compiler.frontend.translator.impl.internal.ITempGenerator

internal class BinaryAssignOperatorVariableLValueExpressionTranslator(
    private val expressionTranslatorStackPusher: IExpressionTranslatorStackPusher,
    private val tempGenerator: ITempGenerator,
    private val operationCodeGenerator: IOperationCodeGenerator,
    private val tempDeclarationCodeGenerator: ITempDeclarationCodeGenerator,
    private val assignCodeGenerator: IAssignCodeGenerator
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
        node.leftExpression as ParsedVariableExpressionNode
        when (location) {
            ExpressionTranslatorLocation.START -> {
                expressionTranslatorStackPusher.push(
                    ExpressionTranslatorLocation.END,
                    node,
                    node.rightExpression,
                    stack
                )
                return tempCounter
            }
            else -> {
                val rightExpression = resultStack.pop()
                val (address, tempAfterAddress) = tempGenerator.generateTempVariable(tempCounter)
                val variableValue = node.leftExpression.value
                val type = variableToTypeMap.getValue(variableValue)
                val tempDeclarationRValue = operationCodeGenerator.generateOperationCode(
                    variableValue,
                    node.operator,
                    rightExpression.address
                )
                val tempDeclarationCode = tempDeclarationCodeGenerator.generateTempDeclarationCode(
                    type,
                    address,
                    tempDeclarationRValue
                )
                val assignCode = assignCodeGenerator.generateAssignCode(
                    variableValue,
                    address
                )
                val code = rightExpression.code + listOf(
                    tempDeclarationCode,
                    assignCode
                )
                val translatedExpressionNode = TranslatedExpressionNode(
                    address,
                    code,
                    type
                )
                resultStack.push(translatedExpressionNode)
                return tempAfterAddress
            }
        }
    }
}