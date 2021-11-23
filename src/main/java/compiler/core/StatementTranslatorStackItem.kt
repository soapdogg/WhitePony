package compiler.core

data class StatementTranslatorStackItem(
    val location: Int,
    val node: IParsedStatementNode
)
