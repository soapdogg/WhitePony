package compiler.frontend.printer.impl

import compiler.core.nodes.parsed.ParsedExpressionStatementNode
import compiler.core.constants.PrinterConstants
import compiler.frontend.printer.impl.internal.IExpressionPrinter
import compiler.frontend.printer.impl.internal.IExpressionStatementPrinter

internal class ExpressionStatementPrinter(
    private val expressionPrinter: IExpressionPrinter
): IExpressionStatementPrinter {
    override fun printNode(node: ParsedExpressionStatementNode): String {
        return expressionPrinter.printNode(node.expressionNode) + PrinterConstants.SEMICOLON
    }
}