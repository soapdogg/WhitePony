package compiler.core

data class TranslatedConstantNode(
    override val address: String,
    override val code: List<String>
): ITranslatedExpressionNode
