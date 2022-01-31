package compiler.frontend.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.IStatementNode
import compiler.core.nodes.translated.TranslatedReturnNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.frontend.printer.impl.internal.ICodeGenerator
import compiler.frontend.printer.impl.internal.IStatementPrinter

internal class TranslatedReturnStatementPrinter(
    private val codeGenerator: ICodeGenerator
): IStatementPrinter {
    override fun printNode(
        node: IStatementNode,
        numberOfTabs: Int,
        location: StatementPrinterLocation,
        stack: Stack<StatementPrinterStackItem>,
        resultStack: Stack<String>,
        appendSemicolon: Boolean
    ) {
        node as TranslatedReturnNode
        val expression = node.expressionStatement.expression
        val returnCode = PrinterConstants.RETURN + PrinterConstants.SPACE + expression.address
        val result = codeGenerator.generateCode(
            expression.code + listOf(returnCode)
        ) + PrinterConstants.SEMICOLON
        resultStack.push(result)
    }
}