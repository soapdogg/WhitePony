package compiler.printer.impl.internal

import compiler.core.IParsedExpressionNode

internal interface IExpressionPrinter {
    fun printParsedNode(node: IParsedExpressionNode): String
}