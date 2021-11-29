package compiler.core.nodes.parsed

data class ParsedWhileNode(
    val expression: IParsedExpressionNode,
    val body: IParsedStatementNode
): IParsedStatementNode
