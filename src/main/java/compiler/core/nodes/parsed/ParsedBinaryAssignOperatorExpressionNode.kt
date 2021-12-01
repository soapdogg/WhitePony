package compiler.core.nodes.parsed

data class ParsedBinaryAssignOperatorExpressionNode(
    override val leftExpression: IParsedExpressionNode,
    override val rightExpression: IParsedExpressionNode,
    val operator: String
): IParsedBinaryExpressionNode
