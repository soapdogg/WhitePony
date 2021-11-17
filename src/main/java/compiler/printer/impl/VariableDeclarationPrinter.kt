package compiler.printer.impl

import compiler.core.ParsedVariableDeclarationNode
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IArrayPrinter
import compiler.printer.impl.internal.IAssignPrinter
import compiler.printer.impl.internal.IVariableDeclarationPrinter

internal class VariableDeclarationPrinter(
    private val arrayPrinter: IArrayPrinter,
    private val assignPrinter: IAssignPrinter
): IVariableDeclarationPrinter {
    override fun printParsedNode(node: ParsedVariableDeclarationNode): String {
        val arrayString = if (node.arrayNode == null) PrinterConstants.EMPTY else arrayPrinter.printParsedNode(node.arrayNode)
        val assignString = if (node.assignNode == null) PrinterConstants.EMPTY else assignPrinter.printParsedNode(node.assignNode)
        return node.id + arrayString + assignString
    }
}