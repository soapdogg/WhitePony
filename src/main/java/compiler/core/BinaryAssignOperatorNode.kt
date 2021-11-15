package compiler.core

data class BinaryAssignOperatorNode(
    val leftExpression: IParsedExpressionNode,
    val rightExpression: IParsedExpressionNode,
    val operator: String
): IParsedExpressionNode
