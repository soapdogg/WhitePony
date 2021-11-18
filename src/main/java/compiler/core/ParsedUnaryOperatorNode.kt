package compiler.core

data class ParsedUnaryOperatorNode(
    override val expression: IParsedExpressionNode,
    val operator: String
): IParsedUnaryExpressionNode
