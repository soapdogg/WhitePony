package compiler.core

data class TranslatedVariableDeclarationNode(
    override val id: String,
    override val arrayNode: TranslatedArrayNode?,
    override val assignNode: TranslatedAssignNode?
): IVariableDeclarationNode