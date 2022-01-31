package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryArrayExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.frontend.translator.impl.internal.IArrayCodeGenerator
import compiler.frontend.translator.impl.internal.IExpressionTranslator
import compiler.frontend.translator.impl.internal.IExpressionTranslatorStackPusher
import compiler.frontend.translator.impl.internal.ITempDeclarationCodeGenerator
import compiler.frontend.translator.impl.internal.ITempGenerator

internal class BinaryArrayExpressionTranslator(
    private val expressionTranslatorStackPusher: IExpressionTranslatorStackPusher,
    private val tempGenerator: ITempGenerator,
    private val arrayCodeGenerator: IArrayCodeGenerator,
    private val tempDeclarationCodeGenerator: ITempDeclarationCodeGenerator
): IExpressionTranslator {
    override fun translate(
        node: IParsedExpressionNode,
        location: ExpressionTranslatorLocation,
        variableToTypeMap: Map<String, String>,
        tempCounter: Int,
        stack: Stack<ExpressionTranslatorStackItem>,
        resultStack: Stack<TranslatedExpressionNode>
    ): Int {
        node as ParsedBinaryArrayExpressionNode
        return when(location) {
            ExpressionTranslatorLocation.START -> {
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
                val (address, t) = tempGenerator.generateTempVariable(tempCounter)
                val variableValue = node.leftExpression.value
                val type = variableToTypeMap.getValue(variableValue)
                val arrayCode = arrayCodeGenerator.generateArrayCode(variableValue, rightExpression.address)

                val tempDeclarationCode = tempDeclarationCodeGenerator.generateTempDeclarationCode(
                    type,
                    address,
                    arrayCode
                )
                val code = rightExpression.code + listOf(tempDeclarationCode)
                val translatedExpressionNode = TranslatedExpressionNode(
                    address,
                    code,
                    type
                )
                resultStack.push(translatedExpressionNode)
                t
            }
        }
    }
}