package compiler.printer.impl.internal

import compiler.core.IFunctionDeclarationNode

internal interface IFunctionDeclarationPrinter {
    fun printNode(node: IFunctionDeclarationNode): String
}