package compiler.core

data class BinaryAssignOperatorNode(
    val leftExpression: IExpressionNode,
    val rightExpression: IExpressionNode,
    val operator: String
): IExpressionNode
