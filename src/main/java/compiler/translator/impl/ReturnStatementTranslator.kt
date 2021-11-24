package compiler.translator.impl

import compiler.core.ParsedReturnNode
import compiler.core.TranslatedReturnNode
import compiler.translator.impl.internal.IExpressionStatementTranslator
import compiler.translator.impl.internal.IReturnStatementTranslator

internal class ReturnStatementTranslator(
    private val expressionStatementTranslator: IExpressionStatementTranslator
): IReturnStatementTranslator {
    override fun translate(
        returnNode: ParsedReturnNode,
        tempCounter: Int
    ): Pair<TranslatedReturnNode, Int> {
        val (expressionStatement, t) = expressionStatementTranslator.translate(
            returnNode.expressionStatement,
            tempCounter
        )
        return Pair(TranslatedReturnNode(expressionStatement), t)
    }
}