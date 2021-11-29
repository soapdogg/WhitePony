package compiler.printer.impl.internal

import compiler.core.nodes.VariableDeclarationListNode

internal interface IVariableDeclarationListPrinter {
    fun printNode(
        node: VariableDeclarationListNode,
        appendSemicolon: Boolean
    ): String
}