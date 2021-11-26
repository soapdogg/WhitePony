package compiler.core.nodes.parsed

interface IParsedUnaryExpressionNode: IParsedExpressionNode {
    val expression: IParsedExpressionNode
}