package compiler.frontend.printer.impl.internal

import compiler.core.nodes.parsed.ParsedReturnNode

internal interface IReturnStatementPrinter {
    fun printNode(node: ParsedReturnNode): String
}