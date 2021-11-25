package compiler.core

data class BooleanExpressionTranslatorStackItem(
    val location: Int,
    val node: IParsedExpressionNode,
    val trueLabel: String,
    val falseLabel: String
)
