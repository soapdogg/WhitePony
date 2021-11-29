package compiler.core.nodes.parsed

data class ParsedExpressionStatementNode (
    val expressionNode: IParsedExpressionNode
): IParsedStatementNode