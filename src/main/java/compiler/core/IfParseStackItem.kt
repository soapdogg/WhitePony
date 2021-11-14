package compiler.core

data class IfParseStackItem(
    val booleanExpression: IExpressionNode
): IParseStackItem {
    override fun getType(): StatementType {
        return StatementType.IF_STATEMENT
    }
}
