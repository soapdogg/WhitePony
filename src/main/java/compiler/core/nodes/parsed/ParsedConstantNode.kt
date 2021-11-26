package compiler.core.nodes.parsed

data class ParsedConstantNode(
    override val value: String,
    val type: String
): IParsedValueExpressionNode
