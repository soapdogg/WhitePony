package compiler.core

data class ParsedUnaryNotOperatorNode(
    val expression: IParsedExpressionNode
): IParsedExpressionNode
