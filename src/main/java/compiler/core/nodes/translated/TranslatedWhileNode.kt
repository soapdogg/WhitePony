package compiler.core.nodes.translated

data class TranslatedWhileNode(
    val expression: ITranslatedExpressionNode,
    val body: ITranslatedStatementNode,
    val falseLabel: String,
    val beginLabel: String,
    val trueLabel: String
): ITranslatedStatementNode
