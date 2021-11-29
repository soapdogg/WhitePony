package compiler.printer.impl.internal

import compiler.core.nodes.IFunctionDeclarationNode

internal interface IFunctionDeclarationPrinter {
    fun printNode(node: IFunctionDeclarationNode, appendSemiColon: Boolean): String
}