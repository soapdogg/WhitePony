package compiler.translator.impl.internal

import compiler.core.ParsedExpressionStatementNode
import compiler.core.TranslatedExpressionStatementNode

internal interface IExpressionStatementTranslator {
    fun translate(
        expressionStatementNode: ParsedExpressionStatementNode,
        variableToTypeMap: Map<String, String>,
        tempCounter: Int
    ): Pair<TranslatedExpressionStatementNode, Int>
}