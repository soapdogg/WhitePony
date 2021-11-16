package compiler.printer.impl.internal

import compiler.core.ParsedVariableDeclarationNode

internal interface IVariableDeclarationPrinter {
    fun printParsedNode(node: ParsedVariableDeclarationNode): String
}