package compiler.core

data class TranslatedExpressionStatementNode(
    val expression: ITranslatedExpressionNode
): ITranslatedStatementNode {
    override fun getNumberOfStatements(): Int {
        return 0
    }
}
