package compiler.core.nodes.translated

data class TranslatedReturnNode(
    val expressionStatement: TranslatedExpressionStatementNode
): ITranslatedStatementNode {
    override fun getNumberOfStatements(): Int {
        return 0
    }
}