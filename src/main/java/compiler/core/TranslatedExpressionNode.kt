package compiler.core

data class TranslatedExpressionNode(
    val address: String,
    val code: List<String>
): ITranslatedExpressionNode
