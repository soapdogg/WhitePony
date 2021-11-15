package compiler.core

data class BinaryAssignNode(
    val leftExpression: IParsedExpressionNode,
    val rightExpression: IParsedExpressionNode
): IParsedExpressionNode
