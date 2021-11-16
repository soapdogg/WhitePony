package compiler.core

data class TranslatedReturnNode(
    val expressionStatement: TranslatedExpressionStatementNode
): ITranslatedStatementNode
