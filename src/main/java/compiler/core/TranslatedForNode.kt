package compiler.core

data class TranslatedForNode(
    val initExpression: ITranslatedExpressionNode,
    val testExpression: ITranslatedExpressionNode,
    val incrementExpression: ITranslatedExpressionNode,
    val body: List<ITranslatedStatementNode>,
    val falseLabel: String,
    val beginLabel: String,
    val trueLabel: String
): ITranslatedStatementNode {
    override fun getNumberOfStatements(): Int {
        return 1
    }
}
