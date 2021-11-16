package compiler.printer.impl

import compiler.core.ParsedReturnNode
import compiler.printer.impl.internal.IExpressionStatementPrinter
import compiler.printer.impl.internal.IReturnStatementPrinter

internal class ReturnStatementPrinter(
    private val expressionStatementPrinter: IExpressionStatementPrinter
): IReturnStatementPrinter {
    override fun printParsedNode(node: ParsedReturnNode): String {
        return "return " + expressionStatementPrinter.printParsedNode(node.expressionStatement)
    }
}