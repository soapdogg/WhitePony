package compiler.core

data class StatementPrinterStackItem(
    val node: IParsedStatementNode,
    val numberOfTabs: Int,
    val location: Int
)
