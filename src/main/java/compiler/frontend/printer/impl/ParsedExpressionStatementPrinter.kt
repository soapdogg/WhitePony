package compiler.frontend.printer.impl

import compiler.core.nodes.IStatementNode
import compiler.core.nodes.parsed.ParsedExpressionStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.frontend.printer.impl.internal.IExpressionStatementPrinter
import compiler.frontend.printer.impl.internal.IStatementPrinter

internal class ParsedExpressionStatementPrinter(
    private val expressionStatementPrinter: IExpressionStatementPrinter
): IStatementPrinter {
    override fun printNode(
        node: IStatementNode,
        numberOfTabs: Int,
        location: StatementPrinterLocation,
        stack: Stack<StatementPrinterStackItem>,
        resultStack: Stack<String>,
        appendSemicolon: Boolean
    ) {
        node as ParsedExpressionStatementNode
        val result = expressionStatementPrinter.printNode(node)
        resultStack.push(result)
    }
}