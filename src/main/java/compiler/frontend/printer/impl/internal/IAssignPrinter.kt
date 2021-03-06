package compiler.frontend.printer.impl.internal

import compiler.core.nodes.AssignNode

internal interface IAssignPrinter {
    fun printNode(node: AssignNode): String
}