package compiler.printer.impl.internal

import compiler.core.ParsedVariableDeclarationListNode

internal interface IVariableDeclarationListPrinter {
    fun printParsedNode(node: ParsedVariableDeclarationListNode): String
}