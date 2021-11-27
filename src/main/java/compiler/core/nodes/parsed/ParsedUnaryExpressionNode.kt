package compiler.core.nodes.parsed

data class ParsedUnaryExpressionNode(
    override val expression: IParsedExpressionNode,
    val operator: String
): IParsedUnaryExpressionNode
