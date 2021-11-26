package compiler.core.nodes

data class VariableDeclarationNode(
    val id: String,
    val arrayNode: ArrayNode?,
    val assignNode: AssignNode?
)
