package compiler.frontend.printer.impl

import compiler.core.nodes.IStatementNode
import compiler.core.nodes.translated.TranslatedBasicBlockNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.frontend.printer.impl.internal.ICodeGenerator
import compiler.frontend.printer.impl.internal.IStatementPrinter
import compiler.frontend.printer.impl.internal.IStatementPrinterStackPusher

internal class TranslatedBasicBlockStatementPrinter(
    private val statementPrinterStackPusher: IStatementPrinterStackPusher,
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
        node as TranslatedBasicBlockNode
        when(location)  {
            StatementPrinterLocation.START -> {
                statementPrinterStackPusher.push(node, numberOfTabs, StatementPrinterLocation.END, stack)
                node.statements.forEach {
                    statementPrinterStackPusher.push(it, numberOfTabs, StatementPrinterLocation.START, stack)
                }
            }
            else -> {
                val basicBlockCode = mutableListOf<String>()
                for (i in 0 until node.statements.size) {
                    basicBlockCode.add(resultStack.pop())
                }
                val result = codeGenerator.generateCode(basicBlockCode)
                resultStack.push(result)
            }
        }
    }
}