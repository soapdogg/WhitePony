package compiler.core

class DoStackItem:IStackItem {
    override fun getType(): StackItemType {
        return StackItemType.DO
    }
}