package compiler.translator.impl

import compiler.core.ParsedReturnNode
import compiler.core.TranslatedReturnNode
import compiler.translator.impl.internal.IExpressionStatementTranslator
import compiler.translator.impl.internal.IReturnStatementTranslator

internal class ReturnStatementTranslator(
    private val expressionStatementTranslator: IExpressionStatementTranslator
): IReturnStatementTranslator {
    override fun translate(returnNode: ParsedReturnNode): TranslatedReturnNode {
        val expressionStatement = expressionStatementTranslator.translate(returnNode.expressionStatement)
        return TranslatedReturnNode(expressionStatement)
    }
}