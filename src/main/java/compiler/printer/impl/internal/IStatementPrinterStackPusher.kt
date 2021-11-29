package compiler.printer.impl.internal

import compiler.core.nodes.IStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem

internal interface IStatementPrinterStackPusher {
    fun push(
        node: IStatementNode,
        numberOfTabs: Int,
        location: StatementPrinterLocation,
        stack: Stack<StatementPrinterStackItem>
    )
}