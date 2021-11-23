package compiler.printer.impl.internal

import compiler.core.VariableDeclarationListNode

internal interface IVariableDeclarationListPrinter {
    fun printNode(node: VariableDeclarationListNode): String
}