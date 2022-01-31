package compiler.frontend.printer.impl

import compiler.core.nodes.VariableDeclarationNode
import compiler.core.constants.PrinterConstants
import compiler.frontend.printer.impl.internal.IArrayPrinter
import compiler.frontend.printer.impl.internal.IAssignPrinter
import compiler.frontend.printer.impl.internal.IVariableDeclarationPrinter

internal class VariableDeclarationPrinter(
    private val arrayPrinter: IArrayPrinter,
    private val assignPrinter: IAssignPrinter
): IVariableDeclarationPrinter {
    override fun printNode(node: VariableDeclarationNode): String {
        val arrayString = if (node.arrayNode == null) PrinterConstants.EMPTY else arrayPrinter.printNode(node.arrayNode)
        val assignString = if (node.assignNode == null) PrinterConstants.EMPTY else assignPrinter.printNode(node.assignNode)
        return node.id + arrayString + assignString
    }
}