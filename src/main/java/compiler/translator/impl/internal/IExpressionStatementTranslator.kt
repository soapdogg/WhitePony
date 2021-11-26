package compiler.translator.impl.internal

import compiler.core.nodes.parsed.ParsedExpressionStatementNode
import compiler.core.nodes.translated.TranslatedExpressionStatementNode

internal interface IExpressionStatementTranslator {
    fun translate(
        expressionStatementNode: ParsedExpressionStatementNode,
        variableToTypeMap: Map<String, String>,
        tempCounter: Int
    ): Pair<TranslatedExpressionStatementNode, Int>
}