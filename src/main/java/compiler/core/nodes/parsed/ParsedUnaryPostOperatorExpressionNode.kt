package compiler.core.nodes.parsed

data class ParsedUnaryPostOperatorExpressionNode(
    override val expression: IParsedExpressionNode,
    val operator: String,
    val oppositeOperator: String
): IParsedUnaryExpressionNode
