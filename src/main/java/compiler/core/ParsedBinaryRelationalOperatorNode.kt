package compiler.core

data class ParsedBinaryRelationalOperatorNode(
    val leftExpression: IParsedExpressionNode,
    val rightExpression: IParsedExpressionNode,
    val operator: String
): IParsedExpressionNode
