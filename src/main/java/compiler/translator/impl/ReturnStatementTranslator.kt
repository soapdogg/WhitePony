package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedReturnNode
import compiler.core.nodes.translated.ITranslatedExpressionNode
import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.nodes.translated.TranslatedReturnNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementTranslatorLocation
import compiler.core.stack.StatementTranslatorStackItem
import compiler.translator.impl.internal.IExpressionStatementTranslator
import compiler.translator.impl.internal.IStatementTranslator

internal class ReturnStatementTranslator(
    private val expressionStatementTranslator: IExpressionStatementTranslator
): IStatementTranslator {

    override fun translate(
        node: IParsedStatementNode,
        location: StatementTranslatorLocation,
        tempCounter: Int,
        labelCounter: Int,
        variableToTypeMap: Map<String, String>,
        stack: Stack<StatementTranslatorStackItem>,
        resultStack: Stack<ITranslatedStatementNode>,
        expressionStack: Stack<ITranslatedExpressionNode>,
        labelStack: Stack<String>
    ): Pair<Int, Int> {
        node as ParsedReturnNode
        val (expressionStatement, tempAfterExpression) = expressionStatementTranslator.translate(
            node.expressionStatement,
            variableToTypeMap,
            tempCounter
        )
        resultStack.push(TranslatedReturnNode(expressionStatement))
        return Pair(tempAfterExpression, labelCounter)
    }
}