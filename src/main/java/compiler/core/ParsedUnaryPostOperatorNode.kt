package compiler.core

data class ParsedUnaryPostOperatorNode(
    override val expression: IParsedExpressionNode,
    val operator: String,
    val oppositeOperator: String
): IParsedUnaryExpressionNode
