package compiler.frontend.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryArrayExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryPostOperatorExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.frontend.translator.impl.internal.*
import compiler.frontend.translator.impl.internal.IArrayCodeGenerator
import compiler.frontend.translator.impl.internal.IExpressionTranslator
import compiler.frontend.translator.impl.internal.IExpressionTranslatorStackPusher
import compiler.frontend.translator.impl.internal.ITempDeclarationCodeGenerator
import compiler.frontend.translator.impl.internal.ITempGenerator

internal class UnaryPostOperatorArrayExpressionTranslator(
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
        node as ParsedUnaryPostOperatorExpressionNode
        node.expression as ParsedBinaryArrayExpressionNode
        when (location) {
            ExpressionTranslatorLocation.START -> {
                expressionTranslatorStackPusher.push(
                    ExpressionTranslatorLocation.END,
                    node,
                    node.expression.rightExpression,
                    stack
                )
                return tempCounter
            }
            else -> {
                val insideExpression = resultStack.pop()
                val (address, tempAfterAddress) = tempGenerator.generateTempVariable(tempCounter)
                val variableValue = node.expression.leftExpression.value
                val type = variableToTypeMap.getValue(variableValue)
                val arrayCode = arrayCodeGenerator.generateArrayCode(
                    variableValue,
                    insideExpression.address
                )
                val tempDeclarationCode = tempDeclarationCodeGenerator.generateTempDeclarationCode(
                    type,
                    address,
                    arrayCode
                )
                val operationCode = operationCodeGenerator.generateOperationCode(
                    address,
                    node.operator,
                    PrinterConstants.ONE
                )
                val operationAssignCode = assignCodeGenerator.generateAssignCode(
                    address,
                    operationCode
                )
                val arrayAssignCode = assignCodeGenerator.generateAssignCode(
                    arrayCode,
                    address
                )
                val oppositeOperationCode = operationCodeGenerator.generateOperationCode(
                    address,
                    node.oppositeOperator,
                    PrinterConstants.ONE
                )
                val oppositeOperationAssignCode = assignCodeGenerator.generateAssignCode(
                    address,
                    oppositeOperationCode
                )
                val code = insideExpression.code +
                        listOf(
                            tempDeclarationCode,
                            operationAssignCode,
                            arrayAssignCode,
                            oppositeOperationAssignCode
                        )
                val translatedExpressionNode = TranslatedExpressionNode(
                    address,
                    code,
                    type,
                )
                resultStack.push(translatedExpressionNode)
                return tempAfterAddress
            }
        }
    }
}