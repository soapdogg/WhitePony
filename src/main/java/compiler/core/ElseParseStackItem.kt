package compiler.core

class ElseParseStackItem : IParseStackItem {
    override fun getType(): StatementType {
        return StatementType.ELSE_STATEMENT
    }
}