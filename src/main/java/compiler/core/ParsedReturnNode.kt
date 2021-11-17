package compiler.core

data class ParsedReturnNode (
    val expressionStatement: ParsedExpressionStatementNode
): IParsedStatementNode {
    override fun getNumberOfStatements(): Int {
        return 0
    }
}