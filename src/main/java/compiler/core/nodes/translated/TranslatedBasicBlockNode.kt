package compiler.core.nodes.translated

import compiler.core.nodes.IBasicBlockNode

data class TranslatedBasicBlockNode(
    override val statements: List<ITranslatedStatementNode>
): ITranslatedStatementNode, IBasicBlockNode {
    override fun getNumberOfStatements(): Int {
        return statements.size
    }
}