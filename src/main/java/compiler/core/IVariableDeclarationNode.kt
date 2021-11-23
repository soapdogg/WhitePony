package compiler.core

interface IVariableDeclarationNode {
    val id: String
    val arrayNode: IArrayNode?
    val assignNode: IAssignNode?
}