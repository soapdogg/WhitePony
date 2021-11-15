package compiler.core

data class UnaryNotOperatorNode(
    val expression: IParsedExpressionNode
): IParsedExpressionNode
