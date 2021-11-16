package compiler.core

data class TranslatedWhileNode(
    val expression: ITranslatedExpressionNode,
    val body: ITranslatedStatementNode
): ITranslatedStatementNode
