package compiler.core

data class VariableDeclarationListNode(
    val type: String,
    val variableDeclarations: List<VariableDeclarationNode>
): IParsedDeclarationStatementNode, ITranslatedDeclarationStatementNode, IParsedStatementNode, ITranslatedStatementNode {
    override fun getNumberOfStatements(): Int {
        return 0
    }
}
