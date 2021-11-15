package compiler.core

data class UnaryOperatorNode(
    val expression: IExpressionNode,
    val operator: String
): IExpressionNode
