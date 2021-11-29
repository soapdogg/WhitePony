package compiler.core.nodes.parsed

data class ParsedReturnNode (
    val expressionStatement: ParsedExpressionStatementNode
): IParsedStatementNode