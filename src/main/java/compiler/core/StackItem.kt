package compiler.core

data class StackItem(
    val node: IParsedStatementNode,
    val numberOfTabs: Int,
    val location: Int
)
