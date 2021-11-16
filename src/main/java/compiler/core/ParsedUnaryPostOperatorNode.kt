package compiler.core

data class ParsedUnaryPostOperatorNode(
    val expression: IParsedExpressionNode,
    val operator: String
): IParsedExpressionNode
