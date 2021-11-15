package compiler.core

data class UnaryNotOperatorNode(
    val expression: IExpressionNode
): IExpressionNode
