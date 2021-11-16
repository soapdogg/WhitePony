package compiler.printer.impl

import compiler.core.IParsedDeclarationStatementNode
import compiler.core.ParsedFunctionDeclarationNode
import compiler.core.ParsedVariableDeclarationListNode
import compiler.printer.impl.internal.IDeclarationStatementPrinter
import compiler.printer.impl.internal.IFunctionDeclarationPrinter
import compiler.printer.impl.internal.IVariableDeclarationListPrinter

internal class DeclarationStatementPrinter(
    private val functionDeclarationPrinter: IFunctionDeclarationPrinter,
    private val variableDeclarationListPrinter: IVariableDeclarationListPrinter
):IDeclarationStatementPrinter {
    override fun printParsedNode(node: IParsedDeclarationStatementNode): String {
        return if (node is ParsedVariableDeclarationListNode) {
            variableDeclarationListPrinter.printParsedNode(node)
        } else {
            functionDeclarationPrinter.printParsedNode(node as ParsedFunctionDeclarationNode)
        }
    }
}