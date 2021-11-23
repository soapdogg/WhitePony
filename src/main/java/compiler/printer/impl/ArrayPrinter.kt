package compiler.printer.impl

import compiler.core.IArrayNode
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IArrayPrinter
import compiler.printer.impl.internal.IExpressionPrinter

internal class ArrayPrinter(
    private val expressionPrinter: IExpressionPrinter
): IArrayPrinter {
    override fun printNode(node: IArrayNode): String {
        val indexString = if (node.indexExpressionNode == null) PrinterConstants.EMPTY else expressionPrinter.printNode(node.indexExpressionNode!!)
        return PrinterConstants.LEFT_BRACKET + indexString + PrinterConstants.RIGHT_BRACKET
    }
}