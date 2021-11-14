package compiler.core

data class BlockParseStackItem(
    val statements: List<IStatementNode>
): IParseStackItem {
    override fun getType(): StatementType {
        return StatementType.BLOCK_STATEMENT
    }
}
