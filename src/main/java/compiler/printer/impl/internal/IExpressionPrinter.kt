package compiler.printer.impl.internal

import compiler.core.IExpressionNode

internal interface IExpressionPrinter {
    fun printNode(node: IExpressionNode): String
}