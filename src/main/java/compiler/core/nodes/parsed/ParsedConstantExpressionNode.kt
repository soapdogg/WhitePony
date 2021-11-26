package compiler.core.nodes.parsed

data class ParsedConstantExpressionNode(
    override val value: String,
    val type: String
): IParsedValueExpressionNode
