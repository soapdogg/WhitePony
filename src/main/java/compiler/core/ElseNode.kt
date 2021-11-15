package compiler.core

data class ElseNode(
    val elseBody: IParsedStatementNode
): IParsedStatementNode
