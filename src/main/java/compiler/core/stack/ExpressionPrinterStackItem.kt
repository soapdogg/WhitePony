package compiler.core.stack

import compiler.core.nodes.IExpressionNode

data class ExpressionPrinterStackItem(
    val node: IExpressionNode,
    val location: Int
)