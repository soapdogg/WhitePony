package compiler.core.nodes.parsed

data class ParsedUnaryPostOperatorNode(
    override val expression: IParsedExpressionNode,
    val operator: String,
    val oppositeOperator: String
): IParsedUnaryExpressionNode
