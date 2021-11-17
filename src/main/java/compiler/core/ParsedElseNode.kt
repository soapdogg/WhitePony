package compiler.core

data class ParsedElseNode(
    val elseBody: IParsedStatementNode
): IParsedStatementNode {
    override fun getNumberOfStatements(): Int {
        return 1
    }
}
