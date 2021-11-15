package compiler.core

data class ConstantNode(
    val value: String,
    val isInt: Boolean
): IExpressionNode
