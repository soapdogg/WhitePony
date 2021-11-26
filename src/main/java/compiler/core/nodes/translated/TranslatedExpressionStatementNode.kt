package compiler.core.nodes.translated

data class TranslatedExpressionStatementNode(
    val expression: TranslatedExpressionNode
): ITranslatedStatementNode {
    override fun getNumberOfStatements(): Int {
        return 0
    }
}
