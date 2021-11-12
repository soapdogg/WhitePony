package compiler.core

data class DoWhileNode(
    val expression: IExpressionNode,
    val body: IStatementNode
): IStatementNode
