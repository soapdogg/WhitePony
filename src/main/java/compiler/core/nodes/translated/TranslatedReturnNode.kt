package compiler.core.nodes.translated

data class TranslatedReturnNode(
    val expressionStatement: TranslatedExpressionStatementNode
): ITranslatedStatementNode
