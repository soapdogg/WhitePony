package compiler.core

data class FakeTranslatedExpressionNode(
    val value: IParsedExpressionNode
): ITranslatedExpressionNode
