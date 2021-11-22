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
    ): TranslatedReturnNode {
        val expressionStatement = expressionStatementTranslator.translate(
            returnNode.expressionStatement,
            labelCounter,
            tempCounter
        )
        return TranslatedReturnNode(expressionStatement)
    }
}