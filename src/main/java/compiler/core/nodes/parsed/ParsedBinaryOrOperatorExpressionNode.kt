package compiler.core.nodes.parsed

data class ParsedBinaryOrOperatorExpressionNode(
    override val leftExpression: IParsedExpressionNode,
    override val rightExpression: IParsedExpressionNode
): IParsedBinaryExpressionNode
