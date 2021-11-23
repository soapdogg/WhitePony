package compiler.core

data class TranslatedBinaryOperatorNode(
    override val address: String,
    override val code: List<String>
): ITranslatedExpressionNode
