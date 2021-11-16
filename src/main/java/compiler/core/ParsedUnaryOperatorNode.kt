package compiler.core

data class ParsedUnaryOperatorNode(
    val expression: IParsedExpressionNode,
    val operator: String
): IParsedExpressionNode
