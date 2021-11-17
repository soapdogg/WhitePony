package compiler.printer.impl

import compiler.core.ParsedExpressionStatementNode
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IExpressionPrinter
import compiler.printer.impl.internal.IExpressionStatementPrinter

internal class ExpressionStatementPrinter(
    private val expressionPrinter: IExpressionPrinter
): IExpressionStatementPrinter {
    override fun printParsedNode(node: ParsedExpressionStatementNode): String {
        return expressionPrinter.printParsedNode(node.expressionNode) + PrinterConstants.SEMICOLON
    }
}