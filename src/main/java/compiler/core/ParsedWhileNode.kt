package compiler.core

data class ParsedWhileNode(
    val expression: IParsedExpressionNode,
    val body: IParsedStatementNode
): IParsedStatementNode
