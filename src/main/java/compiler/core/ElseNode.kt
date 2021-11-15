package compiler.core

data class ElseNode(
    val elseBody: IStatementNode
): IStatementNode
