package compiler.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.IStatementNode
import compiler.core.nodes.parsed.ParsedWhileNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.printer.impl.internal.IExpressionPrinter
import compiler.printer.impl.internal.IStatementPrinter
import compiler.printer.impl.internal.IStatementPrinterStackPusher

internal class ParsedWhileStatementPrinter(
    private val statementPrinterStackPusher: IStatementPrinterStackPusher,
    private val expressionPrinter: IExpressionPrinter
): IStatementPrinter {
    override fun printNode(
        node: IStatementNode,
        numberOfTabs: Int,
        location: StatementPrinterLocation,
        stack: Stack<StatementPrinterStackItem>,
        resultStack: Stack<String>,
        appendSemicolon: Boolean
    ) {
        node as ParsedWhileNode
        when(location) {
            StatementPrinterLocation.START -> {
                statementPrinterStackPusher.push(node, numberOfTabs, StatementPrinterLocation.END, stack)
                statementPrinterStackPusher.push(node.body, numberOfTabs, StatementPrinterLocation.START, stack)
            }
            else -> {
                val bodyString = resultStack.pop()
                val expressionString = expressionPrinter.printNode(node.expression)
                val result = PrinterConstants.WHILE +
                        PrinterConstants.SPACE +
                        expressionString +
                        PrinterConstants.SPACE +
                        bodyString
                resultStack.push(result)
            }
        }

    }
}