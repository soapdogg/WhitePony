package compiler.core

data class ExpressionTranslatorStackItem(
    val location : Int,
    val node: IParsedExpressionNode
)
