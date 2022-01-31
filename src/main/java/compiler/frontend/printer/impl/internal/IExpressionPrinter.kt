package compiler.frontend.printer.impl.internal

import compiler.core.nodes.IExpressionNode

internal interface IExpressionPrinter {
    fun printNode(node: IExpressionNode): String
}