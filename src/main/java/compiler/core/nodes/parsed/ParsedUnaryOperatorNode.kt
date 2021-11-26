package compiler.core.nodes.parsed

data class ParsedUnaryOperatorNode(
    override val expression: IParsedExpressionNode,
    val operator: String
): IParsedUnaryExpressionNode
