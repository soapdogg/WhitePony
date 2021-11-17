package compiler.core

data class ParsedIfNode (
    val booleanExpression: IParsedExpressionNode,
    val ifBody: IParsedStatementNode,
): IParsedStatementNode {
    override fun getNumberOfStatements(): Int {
        return 1
    }
}