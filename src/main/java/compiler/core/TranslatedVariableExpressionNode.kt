package compiler.core

data class TranslatedVariableExpressionNode(
    override val address: String,
    override val code: List<String>
): ITranslatedExpressionNode
