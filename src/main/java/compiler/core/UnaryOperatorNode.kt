package compiler.core

data class UnaryOperatorNode(
    val expression: IParsedExpressionNode,
    val operator: String
): IParsedExpressionNode
