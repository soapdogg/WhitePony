package compiler.frontend.printer.impl

import compiler.core.nodes.IStatementNode
import compiler.core.nodes.translated.TranslatedExpressionStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.frontend.printer.impl.internal.ICodeGenerator
import compiler.frontend.printer.impl.internal.IStatementPrinter

internal class TranslatedExpressionStatementPrinter(
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
        node as TranslatedExpressionStatementNode
        val result =  codeGenerator.generateCode(node.expression.code)
        resultStack.push(result)
    }
}