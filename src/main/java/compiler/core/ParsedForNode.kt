package compiler.core

data class ParsedForNode(
    val initExpression: IParsedExpressionNode,
    val incrementExpression: IParsedExpressionNode,
    val testExpression: IParsedExpressionNode,
    val body: IParsedStatementNode
): IParsedStatementNode
