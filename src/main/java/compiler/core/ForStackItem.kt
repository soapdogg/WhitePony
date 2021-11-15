package compiler.core

data class ForStackItem(
    val initExpression: IExpressionNode,
    val testExpression: IExpressionNode,
    val incrementExpression: IExpressionNode
): IStackItem {
    override fun getType(): StackItemType {
        return StackItemType.FOR
    }
}