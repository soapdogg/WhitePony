package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedReturnNode
import compiler.core.nodes.translated.ITranslatedExpressionNode
import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.nodes.translated.TranslatedExpressionStatementNode
import compiler.core.nodes.translated.TranslatedReturnNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementTranslatorLocation
import compiler.core.stack.StatementTranslatorStackItem
import compiler.frontend.translator.impl.internal.IStatementTranslator

internal class ReturnStatementTranslator(
    private val expressionStatementTranslator: IStatementTranslator
): IStatementTranslator {

    override fun translate(
        node: IParsedStatementNode,
        location: StatementTranslatorLocation,
        tempCounter: Int,
        labelCounter: Int,
        variableToTypeMap: MutableMap<String, String>,
        stack: Stack<StatementTranslatorStackItem>,
        resultStack: Stack<ITranslatedStatementNode>,
        expressionStack: Stack<ITranslatedExpressionNode>,
        labelStack: Stack<String>
    ): Pair<Int, Int> {
        node as ParsedReturnNode
        val (tempAfterExpression, labelAfterExpression) = expressionStatementTranslator.translate(
            node.expressionStatement,
            location,
            tempCounter,
            labelCounter,
            variableToTypeMap,
            stack,
            resultStack,
            expressionStack,
            labelStack
        )
        val expressionStatement = resultStack.pop() as TranslatedExpressionStatementNode
        resultStack.push(TranslatedReturnNode(expressionStatement))
        return Pair(tempAfterExpression, labelAfterExpression)
    }
}