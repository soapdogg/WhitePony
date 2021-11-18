package compiler.core

data class ParsedBinaryAndOperatorNode(
    override val leftExpression: IParsedExpressionNode,
    override val rightExpression: IParsedExpressionNode,
): IParsedBinaryExpressionNode
