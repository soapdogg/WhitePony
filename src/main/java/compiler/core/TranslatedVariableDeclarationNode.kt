package compiler.core

data class TranslatedVariableDeclarationNode(
    val id: String,
    val arrayNode: TranslatedArrayNode?,
    val assignNode: TranslatedAssignNode?
)