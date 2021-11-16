package compiler.printer.impl.internal

import compiler.core.ParsedExpressionStatementNode

internal interface IExpressionStatementPrinter {
    fun printParsedNode(node: ParsedExpressionStatementNode): String
}