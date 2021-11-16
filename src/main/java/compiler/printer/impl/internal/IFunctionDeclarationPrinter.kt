package compiler.printer.impl.internal

import compiler.core.ParsedFunctionDeclarationNode

internal interface IFunctionDeclarationPrinter {
    fun printParsedNode(node: ParsedFunctionDeclarationNode): String
}