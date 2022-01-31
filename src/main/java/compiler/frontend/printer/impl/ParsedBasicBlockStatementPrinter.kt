package compiler.frontend.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.IStatementNode
import compiler.core.nodes.parsed.ParsedBasicBlockNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.frontend.printer.impl.internal.IStatementPrinter
import compiler.frontend.printer.impl.internal.IStatementPrinterStackPusher
import compiler.frontend.printer.impl.internal.ITabsGenerator

internal class ParsedBasicBlockStatementPrinter(
    private val statementPrinterStackPusher: IStatementPrinterStackPusher,
    private val tabsGenerator: ITabsGenerator
) : IStatementPrinter {
    override fun printNode(
        node: IStatementNode,
        numberOfTabs: Int,
        location: StatementPrinterLocation,
        stack: Stack<StatementPrinterStackItem>,
        resultStack: Stack<String>,
        appendSemicolon: Boolean
    ) {
        node as ParsedBasicBlockNode
        when (location) {
            StatementPrinterLocation.START -> {
                statementPrinterStackPusher.push(node, numberOfTabs, StatementPrinterLocation.END, stack)
                node.statements.forEach {
                    statementPrinterStackPusher.push(it, numberOfTabs + 1, StatementPrinterLocation.START, stack)
                }
            }
            else -> {
                val tabs = tabsGenerator.generateTabs(numberOfTabs + 1)
                val closingTabs = tabsGenerator.generateTabs(numberOfTabs)
                val basicBlockStatementCode = mutableListOf<String>()
                for (i in 0 until node.statements.size) {
                    basicBlockStatementCode.add(resultStack.pop())
                }
                val tabbedStatementStrings = basicBlockStatementCode.joinToString(
                    PrinterConstants.NEW_LINE + tabs,
                    PrinterConstants.NEW_LINE + tabs,
                    PrinterConstants.NEW_LINE + closingTabs
                )
                val result = PrinterConstants.LEFT_BRACE +
                        tabbedStatementStrings +
                        PrinterConstants.RIGHT_BRACE
                resultStack.push(result)
            }
        }
    }
}