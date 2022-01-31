package compiler.frontend.printer.impl.internal

import compiler.core.nodes.VariableDeclarationNode

internal interface IVariableDeclarationPrinter {
    fun printNode(node: VariableDeclarationNode): String
}