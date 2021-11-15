package compiler.core

data class UnaryPostOperatorNode(
    val expression: IParsedExpressionNode,
    val operator: String
): IParsedExpressionNode
