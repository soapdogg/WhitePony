package compiler.printer.impl.internal

import compiler.core.ArrayNode

interface IArrayPrinter {
    fun printNode(node: ArrayNode): String
}