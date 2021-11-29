package compiler.printer.impl

import compiler.core.nodes.parsed.ParsedReturnNode
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IExpressionStatementPrinter
import compiler.printer.impl.internal.IReturnStatementPrinter

internal class ReturnStatementPrinter(
    private val expressionStatementPrinter: IExpressionStatementPrinter
): IReturnStatementPrinter {
    override fun printNode(node: ParsedReturnNode): String {
        return PrinterConstants.RETURN + PrinterConstants.SPACE + expressionStatementPrinter.printNode(node.expressionStatement)
    }
}