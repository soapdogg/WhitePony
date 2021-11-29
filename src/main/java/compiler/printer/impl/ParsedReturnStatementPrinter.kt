package compiler.printer.impl

import compiler.core.nodes.IStatementNode
import compiler.core.nodes.parsed.ParsedReturnNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.printer.impl.internal.IReturnStatementPrinter
import compiler.printer.impl.internal.IStatementPrinter

internal class ParsedReturnStatementPrinter(
    private val returnStatementPrinter: IReturnStatementPrinter
): IStatementPrinter {
    override fun printNode(
        node: IStatementNode,
        numberOfTabs: Int,
        location: StatementPrinterLocation,
        stack: Stack<StatementPrinterStackItem>,
        resultStack: Stack<String>,
        appendSemicolon: Boolean
    ) {
        node as ParsedReturnNode
        val result = returnStatementPrinter.printNode(node)
        resultStack.push(result)
    }
}