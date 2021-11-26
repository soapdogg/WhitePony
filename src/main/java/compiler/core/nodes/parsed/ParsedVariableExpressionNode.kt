package compiler.core.nodes.parsed

data class ParsedVariableExpressionNode(
    override val value: String,
): IParsedValueExpressionNode
