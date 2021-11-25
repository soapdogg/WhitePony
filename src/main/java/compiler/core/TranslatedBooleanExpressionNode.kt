package compiler.core

data class TranslatedBooleanExpressionNode(
    override val code: List<String>
): ITranslatedExpressionNode
