package compiler.core.nodes.parsed

data class ParsedUnaryPreOperatorNode(
    override val expression: IParsedExpressionNode,
    val operator: String
): IParsedUnaryExpressionNode
