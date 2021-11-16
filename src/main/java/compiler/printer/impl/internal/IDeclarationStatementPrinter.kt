package compiler.printer.impl.internal

import compiler.core.IParsedDeclarationStatementNode

internal interface IDeclarationStatementPrinter {
    fun printParsedNode(node: IParsedDeclarationStatementNode): String
}