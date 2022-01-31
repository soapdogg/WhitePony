package compiler.frontend.printer.impl

import compiler.core.nodes.IStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.frontend.printer.impl.internal.IStatementPrinterStackPusher

internal class StatementPrinterStackPusher: IStatementPrinterStackPusher {
    override fun push(
        node: IStatementNode,
        numberOfTabs: Int,
        location: StatementPrinterLocation,
        stack: Stack<StatementPrinterStackItem>
    ) {
        stack.push(StatementPrinterStackItem(node, numberOfTabs, location))
    }
}