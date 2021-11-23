package compiler.core.constants

import compiler.core.IVariableDeclarationNode

interface IVariableDeclarationListNode {
    val variableDeclarations: List<IVariableDeclarationNode>
    val type: String
}