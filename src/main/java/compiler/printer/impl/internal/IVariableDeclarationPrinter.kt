package compiler.printer.impl.internal

import compiler.core.VariableDeclarationNode

internal interface IVariableDeclarationPrinter {
    fun printNode(node: VariableDeclarationNode): String
}