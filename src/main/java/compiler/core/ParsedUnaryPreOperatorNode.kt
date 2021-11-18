package compiler.core

data class ParsedUnaryPreOperatorNode(
    override val expression: IParsedExpressionNode,
    val operator: String
): IParsedUnaryExpressionNode
