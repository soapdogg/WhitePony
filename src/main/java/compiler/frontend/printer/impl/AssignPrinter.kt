package compiler.frontend.printer.impl

import compiler.core.nodes.AssignNode
import compiler.core.constants.PrinterConstants
import compiler.frontend.printer.impl.internal.IAssignPrinter
import compiler.frontend.printer.impl.internal.IExpressionPrinter

internal class AssignPrinter(
    private val expressionPrinter: IExpressionPrinter
): IAssignPrinter {
    override fun printNode(node: AssignNode): String {
        return PrinterConstants.SPACE + PrinterConstants.EQUALS + PrinterConstants.SPACE + expressionPrinter.printNode(node.expressionNode)
    }
}