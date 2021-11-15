package compiler.core

data class BinaryAndOperatorNode(
    val leftExpression: IParsedExpressionNode,
    val rightExpression: IParsedExpressionNode
): IParsedExpressionNode
