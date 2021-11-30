package compiler.translator.impl.internal

import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.translated.ITranslatedExpressionNode
import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementTranslatorLocation
import compiler.core.stack.StatementTranslatorStackItem

internal interface IStatementTranslator {
    fun translate(
        node: IParsedStatementNode,
        location: StatementTranslatorLocation,
        tempCounter: Int,
        labelCounter: Int,
        variableToTypeMap: MutableMap<String, String>,
        stack: Stack<StatementTranslatorStackItem>,
        resultStack: Stack<ITranslatedStatementNode>,
        expressionStack: Stack<ITranslatedExpressionNode>,
        labelStack: Stack<String>
    ): Pair<Int, Int>
}