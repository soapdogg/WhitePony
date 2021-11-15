package compiler.core

data class WhileStackItem(
    val expression: IExpressionNode
): IStackItem {
    override fun getType(): StackItemType {
        return StackItemType.WHILE
    }
}