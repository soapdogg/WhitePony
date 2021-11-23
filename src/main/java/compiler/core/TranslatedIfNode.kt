package compiler.core

data class TranslatedIfNode(
    val expression: ITranslatedExpressionNode,
    val ifBody: ITranslatedStatementNode
): ITranslatedStatementNode {
    override fun getNumberOfStatements(): Int {
        return 1
    }
}
