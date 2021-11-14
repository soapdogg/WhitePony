package compiler.core

data class ForParseStackItem(
    val initExpression: IExpressionNode,
    val testExpression: IExpressionNode,
    val incrementExpression: IExpressionNode
): IParseStackItem {
    override fun getType(): StatementType {
        return StatementType.FOR_STATEMENT
    }
}