package compiler.core

data class TranslatedBinaryAssignNode(
    override val address: String,
    override val code: List<String>,
): ITranslatedExpressionNode
