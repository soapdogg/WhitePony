package compiler.core

data class ParsedBasicBlockNode (
    override val statements: List<IParsedStatementNode>
): IParsedStatementNode, IBasicBlockNode  {
    override fun getNumberOfStatements(): Int {
        return statements.size
    }
}