package compiler.core

data class IfStackItem(
    val booleanExpression: IExpressionNode
): IStackItem {
    override fun getType(): StackItemType {
        return StackItemType.IF
    }
}
