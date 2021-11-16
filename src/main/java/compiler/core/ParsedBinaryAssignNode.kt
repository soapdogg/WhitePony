package compiler.core

data class ParsedBinaryAssignNode(
    val leftExpression: IParsedExpressionNode,
    val rightExpression: IParsedExpressionNode
): IParsedExpressionNode
