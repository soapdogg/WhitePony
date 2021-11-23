package compiler.printer.impl.internal

import compiler.core.IAssignNode

internal interface IAssignPrinter {
    fun printNode(node: IAssignNode): String
}