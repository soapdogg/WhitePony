package compiler.core.nodes.parsed

data class ParsedIfNode (
    val booleanExpression: IParsedExpressionNode,
    val ifBody: IParsedStatementNode,
    val elseBody: IParsedStatementNode?,
): IParsedStatementNode {
    override fun getNumberOfStatements(): Int {
        return if(elseBody == null) 1 else 2
    }
}