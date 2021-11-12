package compiler.core

data class ReturnNode (
    val expression: IExpressionNode
): IStatementNode