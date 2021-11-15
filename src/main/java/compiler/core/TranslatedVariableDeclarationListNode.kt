package compiler.core

data class TranslatedVariableDeclarationListNode(
    val type: String,
    val variableDeclarations: List<TranslatedVariableDeclarationNode>
): ITranslatedDeclarationStatementNode