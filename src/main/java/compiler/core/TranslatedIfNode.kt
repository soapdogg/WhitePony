package compiler.core

data class TranslatedIfNode(
    val expression: ITranslatedExpressionNode,
    val ifBody: List<ITranslatedStatementNode>,
    val elseBody: List<ITranslatedStatementNode>,
    val nextLabel: String,
    val trueLabel: String,
    val falseLabel: String
): ITranslatedStatementNode {
    override fun getNumberOfStatements(): Int {
        return ifBody.size
    }
}
