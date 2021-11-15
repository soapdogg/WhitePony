package compiler.core

data class ForNode(
    val initExpression: IParsedExpressionNode,
    val incrementExpression: IParsedExpressionNode,
    val testExpression: IParsedExpressionNode,
    val body: IStatementNode
): IStatementNode
