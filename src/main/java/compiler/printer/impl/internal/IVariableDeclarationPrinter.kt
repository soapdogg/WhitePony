package compiler.printer.impl.internal

import compiler.core.IVariableDeclarationNode

internal interface IVariableDeclarationPrinter {
    fun printNode(node: IVariableDeclarationNode): String
}