package compiler.printer.impl

import compiler.core.ParsedArrayNode
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IArrayPrinter
import compiler.printer.impl.internal.IExpressionPrinter

internal class ArrayPrinter(
    private val expressionPrinter: IExpressionPrinter
): IArrayPrinter {
    override fun printParsedNode(node: ParsedArrayNode): String {
        val indexString = if (node.indexExpressionNode == null) PrinterConstants.EMPTY else expressionPrinter.printParsedNode(node.indexExpressionNode)
        return PrinterConstants.LEFT_BRACKET + indexString + PrinterConstants.RIGHT_BRACKET
    }
}