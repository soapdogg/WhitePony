package compiler.printer.impl.internal

import compiler.core.constants.IVariableDeclarationListNode

internal interface IVariableDeclarationListPrinter {
    fun printNode(node: IVariableDeclarationListNode): String
}