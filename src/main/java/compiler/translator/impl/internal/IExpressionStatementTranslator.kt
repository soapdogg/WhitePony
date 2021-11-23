package compiler.translator.impl.internal

import compiler.core.ParsedExpressionStatementNode
import compiler.core.TranslatedExpressionStatementNode

internal interface IExpressionStatementTranslator {
    fun translate(
        expressionStatementNode: ParsedExpressionStatementNode,
        labelCounter: Int,
        tempCounter: Int
    ): Triple<TranslatedExpressionStatementNode, Int, Int>
}