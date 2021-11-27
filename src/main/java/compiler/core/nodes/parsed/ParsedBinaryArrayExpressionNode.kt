package compiler.core.nodes.parsed

data class ParsedBinaryArrayExpressionNode(
    override val leftExpression: ParsedVariableExpressionNode,
    override val rightExpression: IParsedExpressionNode
): IParsedBinaryExpressionNode
