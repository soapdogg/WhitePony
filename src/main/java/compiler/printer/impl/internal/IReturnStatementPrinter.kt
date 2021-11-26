package compiler.printer.impl.internal

import compiler.core.nodes.parsed.ParsedReturnNode

internal interface IReturnStatementPrinter {
    fun printParsedNode(node: ParsedReturnNode): String
}