package compiler.core

data class FakeTranslatedExpressionNode(
    override val address: String,
    override val code: List<String>,
): ITranslatedExpressionNode
