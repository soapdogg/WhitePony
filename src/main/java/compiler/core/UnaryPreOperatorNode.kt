package compiler.core

data class UnaryPreOperatorNode(
    val expression: IExpressionNode,
    val operator: String
): IExpressionNode
