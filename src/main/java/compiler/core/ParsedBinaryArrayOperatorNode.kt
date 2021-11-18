package compiler.core

data class ParsedBinaryArrayOperatorNode(
    override val leftExpression: ParsedVariableExpressionNode,
    override val rightExpression: IParsedExpressionNode
): IParsedBinaryExpressionNode
