package compiler.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.IStatementNode
import compiler.core.nodes.parsed.ParsedDoWhileNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.printer.impl.internal.IExpressionPrinter
import compiler.printer.impl.internal.IStatementPrinter
import compiler.printer.impl.internal.IStatementPrinterStackPusher

internal class ParsedDoWhileStatementPrinter(
    private val statementPrinterStackPusher: IStatementPrinterStackPusher,
    private val expressionPrinter: IExpressionPrinter,
): IStatementPrinter {
    override fun printNode(
        node: IStatementNode,
        numberOfTabs: Int,
        location: StatementPrinterLocation,
        stack: Stack<StatementPrinterStackItem>,
        resultStack: Stack<String>,
        appendSemicolon: Boolean
    ) {
        node as ParsedDoWhileNode
        when (location) {
            StatementPrinterLocation.START -> {
                statementPrinterStackPusher.push(node, numberOfTabs, StatementPrinterLocation.END_DO_WHILE, stack)
                statementPrinterStackPusher.push(node.body, numberOfTabs, StatementPrinterLocation.START, stack)
            }
            StatementPrinterLocation.END_DO_WHILE -> {
                val bodyString = resultStack.pop()
                val expressionString = expressionPrinter.printNode(node.expression)
                val result = PrinterConstants.DO +
                        PrinterConstants.SPACE +
                        bodyString +
                        PrinterConstants.SPACE +
                        PrinterConstants.WHILE +
                        PrinterConstants.SPACE +
                        expressionString +
                        PrinterConstants.SEMICOLON
                resultStack.push(result)
            }
        }
    }
}