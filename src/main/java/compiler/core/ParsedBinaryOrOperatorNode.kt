package compiler.core

data class ParsedBinaryOrOperatorNode(
    override val leftExpression: IParsedExpressionNode,
    override val rightExpression: IParsedExpressionNode
): IParsedBinaryExpressionNode
