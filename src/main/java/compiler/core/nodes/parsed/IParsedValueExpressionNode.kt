package compiler.core.nodes.parsed

interface IParsedValueExpressionNode: IParsedExpressionNode {
    val value: String
}