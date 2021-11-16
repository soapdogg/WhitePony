package compiler.core

data class ParsedReturnNode (
    val expressionStatement: ParsedExpressionStatementNode
): IParsedStatementNode