package compiler.core

data class ParsedConstantNode(
    override val value: String,
    val isInt: Boolean
): IParsedValueExpressionNode
