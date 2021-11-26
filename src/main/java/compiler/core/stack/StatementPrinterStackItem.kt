package compiler.core.stack

import compiler.core.nodes.IStatementNode

data class StatementPrinterStackItem(
    val node: IStatementNode,
    val numberOfTabs: Int,
    val location: Int
)
