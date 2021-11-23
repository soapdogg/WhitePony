package compiler.printer.impl.internal

import compiler.core.IDeclarationStatementNode

internal interface IDeclarationStatementPrinter {
    fun printNode(node: IDeclarationStatementNode): String
}