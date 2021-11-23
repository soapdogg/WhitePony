package compiler.core

data class TranslatedExpressionStatementNode(
    val expression: TranslatedExpressionNode
): ITranslatedStatementNode {
    override fun getNumberOfStatements(): Int {
        return 0
    }
}
