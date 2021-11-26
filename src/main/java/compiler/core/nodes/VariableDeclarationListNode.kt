package compiler.core.nodes

import compiler.core.nodes.parsed.IParsedDeclarationStatementNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.translated.ITranslatedDeclarationStatementNode
import compiler.core.nodes.translated.ITranslatedStatementNode

data class VariableDeclarationListNode(
    val type: String,
    val variableDeclarations: List<VariableDeclarationNode>
): IParsedDeclarationStatementNode, ITranslatedDeclarationStatementNode, IParsedStatementNode,
    ITranslatedStatementNode {
    override fun getNumberOfStatements(): Int {
        return 0
    }
}
