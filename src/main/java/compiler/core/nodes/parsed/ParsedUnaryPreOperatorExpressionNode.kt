package compiler.core.nodes.parsed

data class ParsedUnaryPreOperatorExpressionNode(
    override val expression: IParsedExpressionNode,
    val operator: String
): IParsedUnaryExpressionNode
