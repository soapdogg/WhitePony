package compiler.core

data class BinaryOperatorNode(
    val leftExpression: IExpressionNode,
    val rightExpression: IExpressionNode,
    val operator: String
): IExpressionNode
