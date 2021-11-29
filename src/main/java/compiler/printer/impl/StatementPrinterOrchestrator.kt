package compiler.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.*
import compiler.core.nodes.parsed.*
import compiler.core.nodes.translated.*
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.printer.impl.internal.ICodeGenerator
import compiler.printer.impl.internal.IExpressionPrinter
import compiler.printer.impl.internal.IGotoCodeGenerator
import compiler.printer.impl.internal.ILabelCodeGenerator
import compiler.printer.impl.internal.IStatementPrinter
import compiler.printer.impl.internal.IStatementPrinterOrchestrator

internal class StatementPrinterOrchestrator(
    private val printerMap: Map<Class<out IStatementNode>, IStatementPrinter>
): IStatementPrinterOrchestrator {
    override fun printNode(node: IStatementNode, numberOfTabs: Int, appendSemicolon: Boolean): String {
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        stack.push(StatementPrinterStackItem(node, numberOfTabs, StatementPrinterLocation.START))
        val wrapInBraces = node is TranslatedBasicBlockNode

        while(stack.isNotEmpty()) {
            val top = stack.pop()
            val printer = printerMap.getValue(top.node.javaClass)
            printer.printNode(
                top.node,
                top.numberOfTabs,
                top.location,
                stack,
                resultStack,
                appendSemicolon
            )
        }

        val top = resultStack.pop()
        return if (wrapInBraces) {
            PrinterConstants.LEFT_BRACE + PrinterConstants.TABBED_NEW_LINE + top + PrinterConstants.NEW_LINE + PrinterConstants.RIGHT_BRACE
        } else {
            top
        }
    }
}