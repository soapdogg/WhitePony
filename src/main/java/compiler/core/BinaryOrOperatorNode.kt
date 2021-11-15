package compiler.core

data class BinaryOrOperatorNode(
    val leftExpression: IParsedExpressionNode,
    val rightExpression: IParsedExpressionNode
): IParsedExpressionNode
