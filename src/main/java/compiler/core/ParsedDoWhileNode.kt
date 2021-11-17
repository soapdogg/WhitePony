package compiler.core

data class ParsedDoWhileNode(
    val expression: IParsedExpressionNode,
    val body: IParsedStatementNode
): IParsedStatementNode {
    override fun getNumberOfStatements(): Int {
        return 1
    }
}
