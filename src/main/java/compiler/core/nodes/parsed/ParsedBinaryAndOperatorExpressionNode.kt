package compiler.core.nodes.parsed

data class ParsedBinaryAndOperatorExpressionNode(
    override val leftExpression: IParsedExpressionNode,
    override val rightExpression: IParsedExpressionNode,
): IParsedBinaryExpressionNode
