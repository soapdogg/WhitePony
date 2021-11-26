package compiler.core.nodes.parsed

data class ParsedBinaryOrOperatorNode(
    override val leftExpression: IParsedExpressionNode,
    override val rightExpression: IParsedExpressionNode
): IParsedBinaryExpressionNode
