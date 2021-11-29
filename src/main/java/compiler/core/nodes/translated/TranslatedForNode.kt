package compiler.core.nodes.translated

data class TranslatedForNode(
    val initExpression: ITranslatedExpressionNode,
    val testExpression: ITranslatedExpressionNode,
    val incrementExpression: ITranslatedExpressionNode,
    val body: ITranslatedStatementNode,
    val falseLabel: String,
    val beginLabel: String,
    val trueLabel: String
): ITranslatedStatementNode
