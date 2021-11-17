package compiler.printer.impl.internal

import compiler.core.IParsedStatementNode
import compiler.core.StatementPrinterStackItem

internal interface IStatementPrinterStackItemGenerator {
    fun generateStatementPrinterStackItems(
        node: IParsedStatementNode,
        numberOfTabs: Int
    ): List<StatementPrinterStackItem>
}