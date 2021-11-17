package compiler.printer.impl.internal

import compiler.core.ParsedAssignNode

internal interface IAssignPrinter {
    fun printParsedNode(node: ParsedAssignNode): String
}