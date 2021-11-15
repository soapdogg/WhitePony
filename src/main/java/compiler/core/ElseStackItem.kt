package compiler.core

class ElseStackItem: IStackItem {
    override fun getType(): StackItemType {
        return StackItemType.ELSE
    }
}