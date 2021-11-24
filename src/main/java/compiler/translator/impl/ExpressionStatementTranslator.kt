package compiler.translator.impl

import compiler.core.ParsedExpressionStatementNode
import compiler.core.TranslatedExpressionStatementNode
import compiler.translator.impl.internal.IExpressionStatementTranslator
import compiler.translator.impl.internal.IExpressionTranslator

internal class ExpressionStatementTranslator(
    private val expressionTranslator: IExpressionTranslator
): IExpressionStatementTranslator {
    override fun translate(
        expressionStatementNode: ParsedExpressionStatementNode,
        variableToTypeMap: Map<String, String>,
        tempCounter: Int,
    ): Pair<TranslatedExpressionStatementNode, Int> {
        val (expression, t) = expressionTranslator.translate(
            expressionStatementNode.expressionNode,
            variableToTypeMap,
            tempCounter
        )
        return Pair(TranslatedExpressionStatementNode(expression), t)
    }
}