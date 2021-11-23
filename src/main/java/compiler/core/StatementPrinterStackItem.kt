package compiler.core

data class StatementPrinterStackItem(
    val node: IStatementNode,
    val numberOfTabs: Int,
    val location: Int
)
