package compiler.printer.impl.internal

import compiler.core.nodes.parsed.ParsedExpressionStatementNode

internal interface IExpressionStatementPrinter {
    fun printNode(node: ParsedExpressionStatementNode): String
}