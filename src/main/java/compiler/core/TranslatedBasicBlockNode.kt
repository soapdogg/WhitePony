package compiler.core

data class TranslatedBasicBlockNode(
    override val statements: List<ITranslatedStatementNode>
): ITranslatedStatementNode, IBasicBlockNode {
    override fun getNumberOfStatements(): Int {
        return statements.size
    }
}