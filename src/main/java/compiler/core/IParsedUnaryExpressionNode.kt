package compiler.core

interface IParsedUnaryExpressionNode: IParsedExpressionNode {
    val expression: IParsedExpressionNode
}