package compiler.printer.impl

import compiler.core.nodes.IDeclarationStatementNode
import compiler.core.nodes.IFunctionDeclarationNode
import compiler.core.nodes.VariableDeclarationListNode
import compiler.printer.impl.internal.IDeclarationStatementPrinter
import compiler.printer.impl.internal.IFunctionDeclarationPrinter
import compiler.printer.impl.internal.IVariableDeclarationListPrinter

internal class DeclarationStatementPrinter(
    private val functionDeclarationPrinter: IFunctionDeclarationPrinter,
    private val variableDeclarationListPrinter: IVariableDeclarationListPrinter
):IDeclarationStatementPrinter {
    override fun printNode(node: IDeclarationStatementNode, appendSemicolon: Boolean): String {
        return if (node is VariableDeclarationListNode) {
            variableDeclarationListPrinter.printNode(
                node,
                true
            )
        } else {
            functionDeclarationPrinter.printNode(node as IFunctionDeclarationNode, appendSemicolon)
        }
    }
}