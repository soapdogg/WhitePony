package compiler.core.nodes.parsed

data class ParsedUnaryNotOperatorNode(
    override val expression: IParsedExpressionNode
): IParsedUnaryExpressionNode
