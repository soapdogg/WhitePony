package compiler.printer.impl.internal

import compiler.core.IArrayNode

interface IArrayPrinter {
    fun printNode(node: IArrayNode): String
}