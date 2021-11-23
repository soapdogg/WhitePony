package compiler.printer.impl.internal

import compiler.core.AssignNode

internal interface IAssignPrinter {
    fun printNode(node: AssignNode): String
}