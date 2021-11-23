package compiler.printer.impl.internal

import compiler.core.IStatementNode
import compiler.core.StatementPrinterStackItem

internal interface IStatementPrinterStackItemGenerator {
    fun generateStatementPrinterStackItems(
        node: IStatementNode,
        numberOfTabs: Int
    ): List<StatementPrinterStackItem>
}