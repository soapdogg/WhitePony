package compiler.core

data class TranslatedExpressionStatementNode(
    val expression: ITranslatedExpressionNode
): ITranslatedStatementNode
