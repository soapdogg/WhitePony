package compiler.core

data class ParsedInnerExpression(
    override val expression: IParsedExpressionNode
): IParsedUnaryExpressionNode
