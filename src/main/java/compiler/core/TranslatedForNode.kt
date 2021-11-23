package compiler.core

data class TranslatedForNode(
    val initExpression: ITranslatedExpressionNode,
    val testExpression: ITranslatedExpressionNode,
    val incrementExpression: ITranslatedExpressionNode,
    val body: ITranslatedStatementNode
): ITranslatedStatementNode {
    override fun getNumberOfStatements(): Int {
        return 1
    }
}
