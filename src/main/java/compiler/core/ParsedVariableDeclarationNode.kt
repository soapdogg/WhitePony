package compiler.core

data class ParsedVariableDeclarationNode (
    val id: String,
    val arrayNode: ArrayNode?,
    val assignNode: AssignNode?
)