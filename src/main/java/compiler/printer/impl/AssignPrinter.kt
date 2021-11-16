package compiler.printer.impl

import compiler.core.ParsedAssignNode
import compiler.printer.impl.internal.IAssignPrinter
import compiler.printer.impl.internal.IExpressionPrinter

internal class AssignPrinter(
    private val expressionPrinter: IExpressionPrinter
): IAssignPrinter {
    override fun printParsedNode(node: ParsedAssignNode?): String {
        return if (node == null) {
            ""
        } else {
            " = " + expressionPrinter.printParsedNode(node.expressionNode)
        }
    }
}