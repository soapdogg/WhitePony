package compiler.core

data class ParsedBinaryAndOperatorNode(
    val leftExpression: IParsedExpressionNode,
    val rightExpression: IParsedExpressionNode
): IParsedExpressionNode
