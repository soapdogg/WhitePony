package compiler.printer.impl

import compiler.core.IAssignNode
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IAssignPrinter
import compiler.printer.impl.internal.IExpressionPrinter

internal class AssignPrinter(
    private val expressionPrinter: IExpressionPrinter
): IAssignPrinter {
    override fun printNode(node: IAssignNode): String {
        return PrinterConstants.SPACE + PrinterConstants.EQUALS + PrinterConstants.SPACE + expressionPrinter.printNode(node.expressionNode)
    }
}