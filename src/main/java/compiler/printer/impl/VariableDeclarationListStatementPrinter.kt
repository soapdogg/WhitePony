package compiler.printer.impl

import compiler.core.nodes.IStatementNode
import compiler.core.nodes.VariableDeclarationListNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.printer.impl.internal.IStatementPrinter
import compiler.printer.impl.internal.IVariableDeclarationListPrinter

internal class VariableDeclarationListStatementPrinter(
    private val variableDeclarationListPrinter: IVariableDeclarationListPrinter
): IStatementPrinter {
    override fun printNode(
        node: IStatementNode,
        numberOfTabs: Int,
        location: StatementPrinterLocation,
        stack: Stack<StatementPrinterStackItem>,
        resultStack: Stack<String>,
        appendSemicolon: Boolean
    ) {
        node as VariableDeclarationListNode
        val result = variableDeclarationListPrinter.printNode(
            node,
            appendSemicolon
        )
        resultStack.push(result)
    }
}