package compiler.core

data class ForNode(
    val initExpression: IExpressionNode,
    val incrementExpression: IExpressionNode,
    val testExpression: IExpressionNode,
    val body: IStatementNode
): IStatementNode
