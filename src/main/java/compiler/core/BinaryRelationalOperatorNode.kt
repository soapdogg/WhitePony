package compiler.core

data class BinaryRelationalOperatorNode(
    val leftExpression: IExpressionNode,
    val rightExpression: IExpressionNode,
    val operator: String
): IExpressionNode
