package compiler.core

data class TranslatedElseNode(
    val elseBody: ITranslatedStatementNode
): ITranslatedStatementNode {
    override fun getNumberOfStatements(): Int {
        return 1
    }
}
