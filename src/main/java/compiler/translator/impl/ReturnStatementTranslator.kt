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
        labelCounter: Int,
        tempCounter: Int
    ): Triple<TranslatedReturnNode, Int, Int> {
        val (expressionStatement, l, t) = expressionStatementTranslator.translate(
            returnNode.expressionStatement,
            labelCounter,
            tempCounter
        )
        return Triple(TranslatedReturnNode(expressionStatement), l, t)
    }
}