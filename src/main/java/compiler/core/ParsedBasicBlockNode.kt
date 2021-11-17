package compiler.core

data class ParsedBasicBlockNode (
    val statements: List<IParsedStatementNode>
): IParsedStatementNode {
    override fun getNumberOfStatements(): Int {
        return statements.size
    }
}