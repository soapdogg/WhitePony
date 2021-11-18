package compiler.core

data class ParsedBinaryAssignNode(
    override val leftExpression: IParsedExpressionNode,
    override val rightExpression: IParsedExpressionNode
): IParsedBinaryExpressionNode
