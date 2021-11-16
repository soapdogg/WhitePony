package compiler.printer.impl.internal

import compiler.core.ParsedReturnNode

internal interface IReturnStatementPrinter {
    fun printParsedNode(node: ParsedReturnNode): String
}