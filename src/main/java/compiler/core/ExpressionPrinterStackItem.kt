package compiler.core

data class ExpressionPrinterStackItem(
    val node: IExpressionNode,
    val location: Int
)