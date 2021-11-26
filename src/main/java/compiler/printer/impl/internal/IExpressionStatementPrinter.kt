package compiler.printer.impl.internal

import compiler.core.nodes.parsed.ParsedExpressionStatementNode

internal interface IExpressionStatementPrinter {
    fun printParsedNode(node: ParsedExpressionStatementNode): String
}