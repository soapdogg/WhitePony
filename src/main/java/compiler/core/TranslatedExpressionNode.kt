package compiler.core

data class TranslatedExpressionNode(
    val address: String,
    val code: List<String>,
    val type: String,
): ITranslatedExpressionNode
