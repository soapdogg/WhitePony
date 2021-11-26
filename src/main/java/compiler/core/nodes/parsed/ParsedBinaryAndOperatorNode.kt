package compiler.core.nodes.parsed

data class ParsedBinaryAndOperatorNode(
    override val leftExpression: IParsedExpressionNode,
    override val rightExpression: IParsedExpressionNode,
): IParsedBinaryExpressionNode
