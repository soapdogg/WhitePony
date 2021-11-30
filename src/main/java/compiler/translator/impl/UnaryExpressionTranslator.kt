package compiler.translator.impl

import compiler.core.constants.TokenizerConstants
import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IExpressionTranslator
import compiler.translator.impl.internal.IExpressionTranslatorStackPusher
import compiler.translator.impl.internal.ITempDeclarationCodeGenerator
import compiler.translator.impl.internal.ITempGenerator

internal class UnaryExpressionTranslator(
    private val expressionTranslatorStackPusher: IExpressionTranslatorStackPusher,
    private val tempGenerator: ITempGenerator,
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
        node as ParsedUnaryExpressionNode
        return when (location) {
            ExpressionTranslatorLocation.START -> {
                expressionTranslatorStackPusher.push(
                    ExpressionTranslatorLocation.END,
                    node,
                    node.expression,
                    stack
                )
                tempCounter
            }
            else -> {
                val expression = resultStack.pop()
                if (node.operator == TokenizerConstants.PLUS_OPERATOR) {
                    resultStack.push(expression)
                    tempCounter
                } else {
                    val (address, tempAfterAddress) = tempGenerator.generateTempVariable(tempCounter)
                    val rValue = node.operator + expression.address
                    val tempDeclarationCode = tempDeclarationCodeGenerator.generateTempDeclarationCode(
                        expression.type,
                        address,
                        rValue
                    )
                    val code = expression.code + listOf(tempDeclarationCode)
                    val translatedExpressionNode = TranslatedExpressionNode(
                        address,
                        code,
                        expression.type
                    )
                    resultStack.push(translatedExpressionNode)
                    tempAfterAddress
                }
            }
        }
    }
}