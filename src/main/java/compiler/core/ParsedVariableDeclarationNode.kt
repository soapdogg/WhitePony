package compiler.core

data class ParsedVariableDeclarationNode (
    val id: String,
    val arrayNode: ParsedArrayNode?,
    val assignNode: ParsedAssignNode?
)