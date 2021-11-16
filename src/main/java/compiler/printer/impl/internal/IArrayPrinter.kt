package compiler.printer.impl.internal

import compiler.core.ParsedArrayNode

interface IArrayPrinter {
    fun printParsedNode(node: ParsedArrayNode?): String
}