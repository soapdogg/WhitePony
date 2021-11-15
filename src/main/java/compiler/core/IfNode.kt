package compiler.core

data class IfNode (
    val booleanExpression: IParsedExpressionNode,
    val ifBody: IParsedStatementNode,
): IParsedStatementNode