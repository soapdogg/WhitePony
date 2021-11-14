package compiler.core

data class WhileParseStackItem(
    val expression: IExpressionNode
): IParseStackItem {
    override fun getType(): StatementType {
        return StatementType.WHILE_STATEMENT
    }
}
