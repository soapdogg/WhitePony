package compiler.core

data class ParsedUnaryPreOperatorNode(
    val expression: IParsedExpressionNode,
    val operator: String
): IParsedExpressionNode
