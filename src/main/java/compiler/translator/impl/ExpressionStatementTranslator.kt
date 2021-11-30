package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedExpressionStatementNode
import compiler.core.nodes.translated.ITranslatedExpressionNode
import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.nodes.translated.TranslatedExpressionStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementTranslatorLocation
import compiler.core.stack.StatementTranslatorStackItem
import compiler.translator.impl.internal.IExpressionTranslatorOrchestrator
import compiler.translator.impl.internal.IStatementTranslator

internal class ExpressionStatementTranslator(
    private val expressionTranslator: IExpressionTranslatorOrchestrator
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
        node as ParsedExpressionStatementNode
        val (expression, tempAfterExpression) = expressionTranslator.translate(
            node.expressionNode,
            variableToTypeMap,
            tempCounter
        )
        resultStack.push(TranslatedExpressionStatementNode(expression))
        return Pair(tempAfterExpression, labelCounter)
    }
}