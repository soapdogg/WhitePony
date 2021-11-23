package compiler.core

data class ParsedConstantNode(
    override val value: String,
    val type: String
): IParsedValueExpressionNode
