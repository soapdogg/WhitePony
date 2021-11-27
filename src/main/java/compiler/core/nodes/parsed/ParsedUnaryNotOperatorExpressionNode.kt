package compiler.core.nodes.parsed

data class ParsedUnaryNotOperatorExpressionNode(
    override val expression: IParsedExpressionNode
): IParsedUnaryExpressionNode
