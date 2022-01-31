package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryPostOperatorExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.frontend.translator.impl.internal.IExpressionTranslator

internal class UnaryPostOperatorExpressionTranslator(
    private val unaryPostOperatorVariableExpressionTranslator: IExpressionTranslator,
    private val unaryPostOperatorArrayExpressionTranslator: IExpressionTranslator
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
        return if (node.expression is ParsedVariableExpressionNode) {
            unaryPostOperatorVariableExpressionTranslator.translate(
                node, location, variableToTypeMap, tempCounter, stack, resultStack
            )
        } else {
            unaryPostOperatorArrayExpressionTranslator.translate(
                node, location, variableToTypeMap, tempCounter, stack, resultStack
            )
        }
    }
}