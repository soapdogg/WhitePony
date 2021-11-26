package compiler.core.nodes.parsed

data class ParsedBinaryArrayOperatorNode(
    override val leftExpression: ParsedVariableExpressionNode,
    override val rightExpression: IParsedExpressionNode
): IParsedBinaryExpressionNode
