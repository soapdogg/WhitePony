package compiler.frontend.printer.impl.internal

import compiler.core.nodes.IDeclarationStatementNode

internal interface IDeclarationStatementPrinter {
    fun printNode(node: IDeclarationStatementNode, appendSemicolon: Boolean): String
}