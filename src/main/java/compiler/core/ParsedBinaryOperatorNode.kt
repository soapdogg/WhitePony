package compiler.core

data class ParsedBinaryOperatorNode(
    val leftExpression: IParsedExpressionNode,
    val rightExpression: IParsedExpressionNode,
    val operator: String
): IParsedExpressionNode
