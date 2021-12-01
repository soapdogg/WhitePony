package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryArrayExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryAssignOperatorExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.translator.impl.internal.*
import compiler.translator.impl.internal.IArrayCodeGenerator
import compiler.translator.impl.internal.IExpressionTranslator
import compiler.translator.impl.internal.IExpressionTranslatorStackPusher
import compiler.translator.impl.internal.ITempDeclarationCodeGenerator
import compiler.translator.impl.internal.ITempGenerator

internal class BinaryAssignOperatorArrayLValueExpressionTranslator(
    private val expressionTranslatorStackPusher: IExpressionTranslatorStackPusher,
    private val tempGenerator: ITempGenerator,
    private val arrayCodeGenerator: IArrayCodeGenerator,
    private val tempDeclarationCodeGenerator: ITempDeclarationCodeGenerator,
    private val operationCodeGenerator: IOperationCodeGenerator,
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
        node.leftExpression as ParsedBinaryArrayExpressionNode
        when (location) {
            ExpressionTranslatorLocation.START -> {
                expressionTranslatorStackPusher.push(
                    ExpressionTranslatorLocation.MIDDLE,
                    node,
                    node.leftExpression.rightExpression,
                    stack
                )
                return tempCounter
            }
            ExpressionTranslatorLocation.MIDDLE -> {
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
                val insideArrayExpression = resultStack.pop()
                val (address, tempAfterAddress) = tempGenerator.generateTempVariable(tempCounter)
                val variableValue = node.leftExpression.leftExpression.value
                val type = variableToTypeMap.getValue(variableValue)
                val arrayCode = arrayCodeGenerator.generateArrayCode(
                    variableValue,
                    insideArrayExpression.address
                )

                val tempDeclarationCode = tempDeclarationCodeGenerator.generateTempDeclarationCode(
                    type,
                    address,
                    arrayCode
                )

                val operationCode = operationCodeGenerator.generateOperationCode(address, node.operator, rightExpression.address)
                val addressAssignCode = assignCodeGenerator.generateAssignCode(
                    address,
                    operationCode
                )
                val arrayAssignCode = assignCodeGenerator.generateAssignCode(
                    arrayCode,
                    address
                )
                val code =
                    insideArrayExpression.code + listOf(tempDeclarationCode) + rightExpression.code + listOf(
                        addressAssignCode,
                        arrayAssignCode
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