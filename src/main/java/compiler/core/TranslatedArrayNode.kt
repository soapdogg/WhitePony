package compiler.core

data class TranslatedArrayNode(
    override val indexExpressionNode: ITranslatedExpressionNode?
): IArrayNode