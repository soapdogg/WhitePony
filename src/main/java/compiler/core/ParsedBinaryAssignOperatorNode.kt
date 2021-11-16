package compiler.core

data class ParsedBinaryAssignOperatorNode(
    val leftExpression: IParsedExpressionNode,
    val rightExpression: IParsedExpressionNode,
    val operator: String
): IParsedExpressionNode
