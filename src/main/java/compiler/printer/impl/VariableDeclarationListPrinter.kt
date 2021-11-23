package compiler.printer.impl

import compiler.core.constants.IVariableDeclarationListNode
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IVariableDeclarationListPrinter
import compiler.printer.impl.internal.IVariableDeclarationPrinter

internal class VariableDeclarationListPrinter(
    private val variableDeclarationPrinter: IVariableDeclarationPrinter
): IVariableDeclarationListPrinter {
    override fun printNode(node: IVariableDeclarationListNode): String {
        val variableDeclarations = node.variableDeclarations.map {
            variableDeclarationPrinter.printNode(it)
        }
        val separator = PrinterConstants.COMMA + PrinterConstants.SPACE
        return node.type + PrinterConstants.SPACE + variableDeclarations.joinToString(separator) + PrinterConstants.SEMICOLON
    }
}