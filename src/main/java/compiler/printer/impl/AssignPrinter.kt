package compiler.printer.impl

import compiler.core.ParsedAssignNode
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IAssignPrinter
import compiler.printer.impl.internal.IExpressionPrinter

internal class AssignPrinter(
    private val expressionPrinter: IExpressionPrinter
): IAssignPrinter {
    override fun printParsedNode(node: ParsedAssignNode): String {
        return PrinterConstants.SPACE + PrinterConstants.EQUALS + PrinterConstants.SPACE + expressionPrinter.printParsedNode(node.expressionNode)
    }
}