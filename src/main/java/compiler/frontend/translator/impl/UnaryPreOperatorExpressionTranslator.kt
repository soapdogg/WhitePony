package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryPreOperatorExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.frontend.translator.impl.internal.IExpressionTranslator

internal class UnaryPreOperatorExpressionTranslator(
    private val unaryPreOperatorVariableExpressionTranslator: IExpressionTranslator,
    private val unaryPreOperatorArrayExpressionTranslator: IExpressionTranslator
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
        return if (node.expression is ParsedVariableExpressionNode) {
            unaryPreOperatorVariableExpressionTranslator.translate(
                node, location, variableToTypeMap, tempCounter, stack, resultStack
            )
        } else {
            unaryPreOperatorArrayExpressionTranslator.translate(
                node, location, variableToTypeMap, tempCounter, stack, resultStack
            )
        }
    }
}