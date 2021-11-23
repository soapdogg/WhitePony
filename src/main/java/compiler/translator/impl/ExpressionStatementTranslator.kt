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
        labelCounter: Int,
        tempCounter: Int,
    ): Triple<TranslatedExpressionStatementNode, Int, Int> {
        val (expression, l, t) = expressionTranslator.translate(
            expressionStatementNode.expressionNode,
            labelCounter,
            tempCounter
        )
        return Triple(TranslatedExpressionStatementNode(expression), l, t)
    }
}