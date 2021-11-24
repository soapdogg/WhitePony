package compiler.translator.impl.internal

import compiler.core.ParsedExpressionStatementNode
import compiler.core.TranslatedExpressionStatementNode

internal interface IExpressionStatementTranslator {
    fun translate(
        expressionStatementNode: ParsedExpressionStatementNode,
        tempCounter: Int
    ): Pair<TranslatedExpressionStatementNode, Int>
}