package compiler.core.nodes.translated

data class TranslatedDoWhileNode(
    val expressionNode: ITranslatedExpressionNode,
    val body: List<ITranslatedStatementNode>,
    val falseLabel: String,
    val trueLabel: String
): ITranslatedStatementNode {
    override fun getNumberOfStatements(): Int {
        return 1
    }
}
