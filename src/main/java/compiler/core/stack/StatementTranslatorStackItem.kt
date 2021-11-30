package compiler.core.stack

import compiler.core.nodes.parsed.IParsedStatementNode

data class StatementTranslatorStackItem(
    val location: StatementTranslatorLocation,
    val node: IParsedStatementNode
)
