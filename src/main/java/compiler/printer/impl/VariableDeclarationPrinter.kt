package compiler.printer.impl

import compiler.core.ParsedVariableDeclarationNode
import compiler.printer.impl.internal.IArrayPrinter
import compiler.printer.impl.internal.IAssignPrinter
import compiler.printer.impl.internal.IVariableDeclarationPrinter

internal class VariableDeclarationPrinter(
    private val arrayPrinter: IArrayPrinter,
    private val assignNode: IAssignPrinter
): IVariableDeclarationPrinter {
    override fun printParsedNode(node: ParsedVariableDeclarationNode): String {
        return node.id + arrayPrinter.printParsedNode(node.arrayNode) + assignNode.printParsedNode(node.assignNode)
    }
}