package compiler.core

data class TranslatedIfNode(
    val expression: ITranslatedExpressionNode,
    val ifBody: ITranslatedStatementNode
): ITranslatedStatementNode
