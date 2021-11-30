package compiler.translator.impl

import compiler.core.nodes.parsed.ParsedBinaryOperatorExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IBinaryOperatorExpressionTranslator
import compiler.translator.impl.internal.IExpressionTranslatorStackPusher
import compiler.translator.impl.internal.IOperationCodeGenerator
import compiler.translator.impl.internal.ITempDeclarationCodeGenerator
import compiler.translator.impl.internal.ITempGenerator
import compiler.translator.impl.internal.ITypeDeterminer

internal class BinaryOperatorExpressionTranslator(
    private val expressionTranslatorStackPusher: IExpressionTranslatorStackPusher,
    private val tempGenerator: ITempGenerator,
    private val typeDeterminer: ITypeDeterminer,
    private val operationCodeGenerator: IOperationCodeGenerator,
    private val tempDeclarationCodeGenerator: ITempDeclarationCodeGenerator
): IBinaryOperatorExpressionTranslator {
    override fun translate(
        node: ParsedBinaryOperatorExpressionNode,
        location: ExpressionTranslatorLocation,
        tempCounter: Int,
        variableToTypeMap: Map<String, String>,
        stack: Stack<ExpressionTranslatorStackItem>,
        resultStack: Stack<TranslatedExpressionNode>
    ): Int {
        return when (location) {
            ExpressionTranslatorLocation.START -> {
                expressionTranslatorStackPusher.push(
                    ExpressionTranslatorLocation.MIDDLE,
                    node,
                    node.leftExpression,
                    stack
                )
                tempCounter
            }
            ExpressionTranslatorLocation.MIDDLE -> {
                expressionTranslatorStackPusher.push(
                    ExpressionTranslatorLocation.END,
                    node,
                    node.rightExpression,
                    stack
                )
                tempCounter
            }
            else -> {
                val rightExpression = resultStack.pop()
                val leftExpression = resultStack.pop()
                val (address, t) = tempGenerator.generateTempVariable(tempCounter)
                val type = typeDeterminer.determineType(leftExpression.type, rightExpression.type)
                val operationCode = operationCodeGenerator.generateOperationCode(
                    leftExpression.address,
                    node.operator,
                    rightExpression.address
                )
                val tempDeclarationCode = tempDeclarationCodeGenerator.generateTempDeclarationCode(
                    type,
                    address,
                    operationCode
                )

                val code = leftExpression.code +
                        rightExpression.code +
                        listOf(tempDeclarationCode)

                val translatedBinaryOperatorNode = TranslatedExpressionNode(
                    address,
                    code,
                    type
                )
                resultStack.push(translatedBinaryOperatorNode)
                t
            }
        }
    }
}