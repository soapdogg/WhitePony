package compiler.frontend.printer.impl

import compiler.core.nodes.VariableDeclarationListNode
import compiler.core.constants.PrinterConstants
import compiler.frontend.printer.impl.internal.IVariableDeclarationListPrinter
import compiler.frontend.printer.impl.internal.IVariableDeclarationPrinter

internal class VariableDeclarationListPrinter(
    private val variableDeclarationPrinter: IVariableDeclarationPrinter
): IVariableDeclarationListPrinter {
    override fun printNode(
        node: VariableDeclarationListNode,
        appendSemicolon: Boolean
    ): String {
        val variableDeclarations = node.variableDeclarations.map {
            variableDeclarationPrinter.printNode(it)
        }
        val separator = PrinterConstants.COMMA + PrinterConstants.SPACE
        val result = node.type + PrinterConstants.SPACE + variableDeclarations.joinToString(separator)
        if (appendSemicolon) {
            return result + PrinterConstants.SEMICOLON
        }
        return result
    }
}