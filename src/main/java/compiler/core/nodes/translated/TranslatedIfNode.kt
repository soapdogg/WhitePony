package compiler.core.nodes.translated

data class TranslatedIfNode(
    val expression: ITranslatedExpressionNode,
    val ifBody: ITranslatedStatementNode,
    val elseBody: ITranslatedStatementNode?,
    val nextLabel: String,
    val trueLabel: String,
    val falseLabel: String
): ITranslatedStatementNode
