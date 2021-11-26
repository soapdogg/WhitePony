package compiler.core.nodes.parsed

interface IParsedBinaryExpressionNode: IParsedExpressionNode {
    val leftExpression: IParsedExpressionNode
    val rightExpression: IParsedExpressionNode
}