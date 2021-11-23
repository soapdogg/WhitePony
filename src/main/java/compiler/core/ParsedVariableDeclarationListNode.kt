package compiler.core

import compiler.core.constants.IVariableDeclarationListNode

data class ParsedVariableDeclarationListNode(
    override val type: String,
    override val variableDeclarations: List<ParsedVariableDeclarationNode>
) : IParsedDeclarationStatementNode, IParsedStatementNode, IVariableDeclarationListNode {
    override fun getNumberOfStatements(): Int {
        return 0
    }
}