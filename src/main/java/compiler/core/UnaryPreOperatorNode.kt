package compiler.core

data class UnaryPreOperatorNode(
    val expression: IParsedExpressionNode,
    val operator: String
): IParsedExpressionNode
