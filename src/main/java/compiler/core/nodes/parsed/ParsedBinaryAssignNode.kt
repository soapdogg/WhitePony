package compiler.core.nodes.parsed

data class ParsedBinaryAssignNode(
    override val leftExpression: IParsedExpressionNode,
    override val rightExpression: IParsedExpressionNode
): IParsedBinaryExpressionNode
