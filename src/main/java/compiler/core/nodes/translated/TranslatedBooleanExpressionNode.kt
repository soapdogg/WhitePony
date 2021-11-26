package compiler.core.nodes.translated

data class TranslatedBooleanExpressionNode(
    override val code: List<String>
): ITranslatedExpressionNode
