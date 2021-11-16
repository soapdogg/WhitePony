package compiler.core

data class TranslatedElseNode(
    val elseBody: ITranslatedStatementNode
): ITranslatedStatementNode
