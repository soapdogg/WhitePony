package compiler.core

data class BinaryRelationalOperatorNode(
    val leftExpression: IParsedExpressionNode,
    val rightExpression: IParsedExpressionNode,
    val operator: String
): IParsedExpressionNode
