package compiler.core.nodes.parsed

data class ParsedBinaryAssignExpressionNode(
    override val leftExpression: IParsedExpressionNode,
    override val rightExpression: IParsedExpressionNode
): IParsedBinaryExpressionNode
