package compiler.core

data class ParsedElseNode(
    val elseBody: IParsedStatementNode
): IParsedStatementNode
