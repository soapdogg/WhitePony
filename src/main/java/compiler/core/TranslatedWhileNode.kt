package compiler.core

data class TranslatedWhileNode(
    val expression: ITranslatedExpressionNode,
    val body: List<ITranslatedStatementNode>,
    val falseLabel: String,
    val beginLabel: String,
    val trueLabel: String
): ITranslatedStatementNode {
    override fun getNumberOfStatements(): Int {
        return 1
    }
}
