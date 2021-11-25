package compiler.core

data class TranslatedExpressionNode(
    val address: String,
    override val code: List<String>,
    val type: String,
): ITranslatedExpressionNode
