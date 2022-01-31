package compiler.frontend.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryPreOperatorExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.frontend.translator.impl.internal.IAssignCodeGenerator
import compiler.frontend.translator.impl.internal.IExpressionTranslator
import compiler.frontend.translator.impl.internal.IOperationCodeGenerator

internal class UnaryPreOperatorVariableExpressionTranslator(
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
        node as ParsedUnaryPreOperatorExpressionNode
        node.expression as ParsedVariableExpressionNode
        val variableValue = node.expression.value
        val operationCode = operationCodeGenerator.generateOperationCode(
            variableValue,
            node.operator,
            PrinterConstants.ONE
        )
        val assignCode = assignCodeGenerator.generateAssignCode(
            variableValue,
            operationCode
        )
        val code = listOf(assignCode)
        val type = variableToTypeMap.getValue(variableValue)
        val translatedExpressionNode = TranslatedExpressionNode(
            variableValue,
            code,
            type
        )
        resultStack.push(translatedExpressionNode)
        return tempCounter
    }
}