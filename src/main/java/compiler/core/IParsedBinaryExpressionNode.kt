package compiler.core

interface IParsedBinaryExpressionNode: IParsedExpressionNode {
    val leftExpression: IParsedExpressionNode
    val rightExpression: IParsedExpressionNode
}