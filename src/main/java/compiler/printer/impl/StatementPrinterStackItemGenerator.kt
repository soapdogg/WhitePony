package compiler.printer.impl

import compiler.core.*
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IStatementPrinterStackItemGenerator

internal class StatementPrinterStackItemGenerator: IStatementPrinterStackItemGenerator {
    override fun generateStatementPrinterStackItems(node: IParsedStatementNode, numberOfTabs: Int): List<StatementPrinterStackItem> {
        val returnStackItem = StatementPrinterStackItem(node, numberOfTabs, PrinterConstants.LOCATION_2)
        val stackItems = mutableListOf(returnStackItem)
        when(node) {
            is ParsedBasicBlockNode -> {
                node.statements.forEach {
                    val stackItem = StatementPrinterStackItem(it, numberOfTabs + 1, PrinterConstants.LOCATION_1)
                    stackItems.add(stackItem)
                }
            }
            is ParsedDoWhileNode -> {
                val stackItem = StatementPrinterStackItem(node.body, numberOfTabs, PrinterConstants.LOCATION_1)
                stackItems.add(stackItem)
            }
            is ParsedWhileNode -> {
                val stackItem = StatementPrinterStackItem(node.body, numberOfTabs, PrinterConstants.LOCATION_1)
                stackItems.add(stackItem)
            }
            is ParsedForNode -> {
                val stackItem = StatementPrinterStackItem(node.body, numberOfTabs, PrinterConstants.LOCATION_1)
                stackItems.add(stackItem)
            }
            is ParsedIfNode -> {
                val stackItem = StatementPrinterStackItem(node.ifBody, numberOfTabs, PrinterConstants.LOCATION_1)
                stackItems.add(stackItem)
            }
            is ParsedElseNode -> {
                val stackItem = StatementPrinterStackItem(node.elseBody, numberOfTabs, PrinterConstants.LOCATION_1)
                stackItems.add(stackItem)
            }
        }
        return stackItems
    }
}