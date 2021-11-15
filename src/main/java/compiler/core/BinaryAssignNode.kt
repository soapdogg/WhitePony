package compiler.core

data class BinaryAssignNode(
    val leftExpression: IExpressionNode,
    val rightExpression: IExpressionNode
): IExpressionNode
