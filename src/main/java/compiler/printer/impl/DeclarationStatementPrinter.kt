package compiler.printer.impl

import compiler.core.*
import compiler.core.constants.IVariableDeclarationListNode
import compiler.printer.impl.internal.IDeclarationStatementPrinter
import compiler.printer.impl.internal.IFunctionDeclarationPrinter
import compiler.printer.impl.internal.IVariableDeclarationListPrinter

internal class DeclarationStatementPrinter(
    private val functionDeclarationPrinter: IFunctionDeclarationPrinter,
    private val variableDeclarationListPrinter: IVariableDeclarationListPrinter
):IDeclarationStatementPrinter {
    override fun printNode(node: IDeclarationStatementNode): String {
        return if (node is IVariableDeclarationListNode) {
            variableDeclarationListPrinter.printNode(node)
        } else {
            functionDeclarationPrinter.printNode(node as IFunctionDeclarationNode)
        }
    }
}