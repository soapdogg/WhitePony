package compiler.core

data class WhileNode(
    val expression: IExpressionNode,
    val body: IStatementNode
): IStatementNode
