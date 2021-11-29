package compiler.printer.impl.internal

import compiler.core.nodes.ArrayNode

internal interface IArrayPrinter {
    fun printNode(node: ArrayNode): String
}