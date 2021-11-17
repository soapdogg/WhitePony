package compiler.core

data class ParsedExpressionStatementNode (
    val expressionNode: IParsedExpressionNode
):IParsedStatementNode {
    override fun getNumberOfStatements(): Int {
        return 0
    }
}