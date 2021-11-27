package compiler.core.nodes.parsed

data class ParsedWhileNode(
    val expression: IParsedExpressionNode,
    val body: IParsedStatementNode
): IParsedStatementNode {
    override fun getNumberOfStatements(): Int {
        return body.getNumberOfStatements()
    }
}