package compiler.translator.impl

import compiler.core.nodes.parsed.ParsedReturnNode
import compiler.core.nodes.translated.TranslatedReturnNode
import compiler.translator.impl.internal.IExpressionStatementTranslator
import compiler.translator.impl.internal.IReturnStatementTranslator

internal class ReturnStatementTranslator(
    private val expressionStatementTranslator: IExpressionStatementTranslator
): IReturnStatementTranslator {
    override fun translate(
        returnNode: ParsedReturnNode,
        variableToTypeMap: Map<String, String>,
        tempCounter: Int
    ): Pair<TranslatedReturnNode, Int> {
        val (expressionStatement, t) = expressionStatementTranslator.translate(
            returnNode.expressionStatement,
            variableToTypeMap,
            tempCounter
        )
        return Pair(TranslatedReturnNode(expressionStatement), t)
    }
}