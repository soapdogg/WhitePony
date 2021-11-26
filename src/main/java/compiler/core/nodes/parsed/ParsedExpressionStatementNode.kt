package compiler.core.nodes.parsed

data class ParsedExpressionStatementNode (
    val expressionNode: IParsedExpressionNode
): IParsedStatementNode {
    override fun getNumberOfStatements(): Int {
        return 1
    }
}