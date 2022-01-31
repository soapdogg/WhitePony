package compiler.frontend.translator.impl

import compiler.core.nodes.VariableDeclarationListNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.translated.ITranslatedExpressionNode
import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementTranslatorLocation
import compiler.core.stack.StatementTranslatorStackItem
import compiler.frontend.translator.impl.internal.IStatementTranslator
import compiler.frontend.translator.impl.internal.IVariableTypeRecorder

internal class VariableDeclarationStatementTranslator(
    private val variableTypeRecorder: IVariableTypeRecorder
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
        node as VariableDeclarationListNode
        variableTypeRecorder.recordVariableTypes(
            node,
            variableToTypeMap
        )
        resultStack.push(node)
        return Pair(tempCounter, labelCounter)
    }
}