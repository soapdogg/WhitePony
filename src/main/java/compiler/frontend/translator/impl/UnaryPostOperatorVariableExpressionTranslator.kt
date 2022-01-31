package compiler.frontend.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryPostOperatorExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.frontend.translator.impl.internal.IAssignCodeGenerator
import compiler.frontend.translator.impl.internal.IExpressionTranslator
import compiler.frontend.translator.impl.internal.IOperationCodeGenerator
import compiler.frontend.translator.impl.internal.ITempDeclarationCodeGenerator
import compiler.frontend.translator.impl.internal.ITempGenerator

internal class UnaryPostOperatorVariableExpressionTranslator(
    private val tempGenerator: ITempGenerator,
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
        node.expression as ParsedVariableExpressionNode
        val variableValue = node.expression.value
        val (address, tempAfterAddress) = tempGenerator.generateTempVariable(tempCounter)
        val type = variableToTypeMap.getValue(variableValue)
        val tempDeclarationCode = tempDeclarationCodeGenerator.generateTempDeclarationCode(
            type,
            address,
            variableValue
        )
        val operationCode = operationCodeGenerator.generateOperationCode(
            variableValue,
            node.operator,
            PrinterConstants.ONE
        )
        val assignCode = assignCodeGenerator.generateAssignCode(
            variableValue,
            operationCode
        )
        val code = listOf(
            tempDeclarationCode,
            assignCode
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