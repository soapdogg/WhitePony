package compiler.printer.impl

import compiler.core.ParsedArrayNode
import compiler.printer.impl.internal.IArrayPrinter
import compiler.printer.impl.internal.IExpressionPrinter

internal class ArrayPrinter(
    private val expressionPrinter: IExpressionPrinter
): IArrayPrinter {
    override fun printParsedNode(node: ParsedArrayNode?): String {
        return if (node == null) {
            ""
        } else {
            if (node.indexExpressionNode != null) {
                "[" + expressionPrinter.printParsedNode(node.indexExpressionNode) + "]"
            }
            else {
                "[]"
            }
        }
    }
}