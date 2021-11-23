package compiler.core

data class ParsedVariableDeclarationNode (
    override val id: String,
    override val arrayNode: ParsedArrayNode?,
    override val assignNode: ParsedAssignNode?
): IVariableDeclarationNode