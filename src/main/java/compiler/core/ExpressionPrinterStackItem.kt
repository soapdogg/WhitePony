package compiler.core

data class ExpressionPrinterStackItem(
    val node: IParsedExpressionNode,
    val location: Int
)