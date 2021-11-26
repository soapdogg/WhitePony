package compiler.printer.impl.internal

import compiler.core.nodes.ArrayNode

interface IArrayPrinter {
    fun printNode(node: ArrayNode): String
}