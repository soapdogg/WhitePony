package compiler.core.nodes.translated

data class TranslatedDoWhileNode(
    val expressionNode: ITranslatedExpressionNode,
    val body: ITranslatedStatementNode,
    val falseLabel: String,
    val trueLabel: String
): ITranslatedStatementNode
