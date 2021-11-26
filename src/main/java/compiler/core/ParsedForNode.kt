package compiler.core

data class ParsedForNode(
    val initExpression: IParsedExpressionNode,
    val testExpression: IParsedExpressionNode,
    val incrementExpression: IParsedExpressionNode,
    val body: IParsedStatementNode
): IParsedStatementNode {
    override fun getNumberOfStatements(): Int {
        return body.getNumberOfStatements()
    }
}
