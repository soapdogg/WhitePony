package compiler.core.nodes.parsed

data class ParsedIfNode (
    val booleanExpression: IParsedExpressionNode,
    val ifBody: IParsedStatementNode,
    val elseBody: IParsedStatementNode?,
): IParsedStatementNode