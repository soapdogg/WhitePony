package compiler.core.nodes.parsed

data class ParsedReturnNode (
    val expressionStatement: ParsedExpressionStatementNode
): IParsedStatementNode {
    override fun getNumberOfStatements(): Int {
        return 0
    }
}