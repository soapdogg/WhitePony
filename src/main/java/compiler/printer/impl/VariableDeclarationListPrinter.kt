package compiler.printer.impl

import compiler.core.ParsedVariableDeclarationListNode
import compiler.printer.impl.internal.IVariableDeclarationListPrinter
import compiler.printer.impl.internal.IVariableDeclarationPrinter

internal class VariableDeclarationListPrinter(
    private val variableDeclarationPrinter: IVariableDeclarationPrinter
): IVariableDeclarationListPrinter {
    override fun printParsedNode(node: ParsedVariableDeclarationListNode): String {
        val variableDeclarations = node.variableDeclarations.map {
            variableDeclarationPrinter.printParsedNode(it)
        }
        return node.type + " " + variableDeclarations.joinToString(", ") + ";"
    }
}