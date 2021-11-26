package compiler.core.nodes.parsed

data class ParsedInnerExpressionNode(
    override val expression: IParsedExpressionNode
): IParsedUnaryExpressionNode
