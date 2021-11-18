package compiler.core

data class ParsedUnaryNotOperatorNode(
    override val expression: IParsedExpressionNode
): IParsedUnaryExpressionNode
