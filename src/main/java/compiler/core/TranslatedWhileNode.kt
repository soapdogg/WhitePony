package compiler.core

data class TranslatedWhileNode(
    val expression: ITranslatedExpressionNode,
    val body: ITranslatedStatementNode
): ITranslatedStatementNode {
    override fun getNumberOfStatements(): Int {
        return 1
    }
}
