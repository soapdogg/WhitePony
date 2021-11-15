package compiler.core

data class UnaryPostOperatorNode(
    val expression: IExpressionNode,
    val operator: String
): IExpressionNode
