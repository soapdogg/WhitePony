package compiler.core.nodes.parsed

data class ParsedBinaryOperatorNode(
    override val leftExpression: IParsedExpressionNode,
    override val rightExpression: IParsedExpressionNode,
    val operator: String
): IParsedBinaryExpressionNode
