package compiler.core

class DoParseStackItem : IParseStackItem {
    override fun getType(): StatementType {
        return StatementType.DO_STATEMENT
    }
}
