package compiler.core

data class VariableDeclarationListNode(
    val type: String,
    val variableDeclarations: List<VariableDeclarationNode>
) : IParsedDeclarationStatementNode, IStatementNode