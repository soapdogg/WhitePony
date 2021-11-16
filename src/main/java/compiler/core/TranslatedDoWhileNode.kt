package compiler.core

data class TranslatedDoWhileNode(
    val expressionNode: ITranslatedExpressionNode,
    val body: ITranslatedStatementNode
): ITranslatedStatementNode
