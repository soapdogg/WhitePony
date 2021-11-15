package compiler.core

data class BinaryOrOperatorNode(
    val leftExpression: IExpressionNode,
    val rightExpression: IExpressionNode
): IExpressionNode
