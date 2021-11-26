package compiler.core.nodes.translated

data class TranslatedExpressionNode(
    val address: String,
    override val code: List<String>,
    val type: String,
): ITranslatedExpressionNode
