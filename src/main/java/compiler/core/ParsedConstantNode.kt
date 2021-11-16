package compiler.core

data class ParsedConstantNode(
    val value: String,
    val isInt: Boolean
): IParsedExpressionNode
