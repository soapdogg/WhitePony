package compiler.core

data class BinaryAndOperatorNode(
    val leftExpression: IExpressionNode,
    val rightExpression: IExpressionNode
): IExpressionNode
