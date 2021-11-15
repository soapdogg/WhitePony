package compiler.core

data class IfNode (
    val booleanExpression: IExpressionNode,
    val ifBody: IStatementNode,
): IStatementNode