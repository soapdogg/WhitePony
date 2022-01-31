package compiler.frontend.printer.impl

import compiler.core.nodes.IStatementNode
import compiler.core.nodes.translated.TranslatedDoWhileNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.frontend.printer.impl.internal.ICodeGenerator
import compiler.frontend.printer.impl.internal.ILabelCodeGenerator
import compiler.frontend.printer.impl.internal.IStatementPrinter
import compiler.frontend.printer.impl.internal.IStatementPrinterStackPusher

internal class TranslatedDoWhileStatementPrinter(
    private val statementPrinterStackPusher: IStatementPrinterStackPusher,
    private val codeGenerator: ICodeGenerator,
    private val labelCodeGenerator: ILabelCodeGenerator
): IStatementPrinter {
    override fun printNode(
        node: IStatementNode,
        numberOfTabs: Int,
        location: StatementPrinterLocation,
        stack: Stack<StatementPrinterStackItem>,
        resultStack: Stack<String>,
        appendSemicolon: Boolean
    ) {
        node as TranslatedDoWhileNode
        when(location) {
            StatementPrinterLocation.START -> {
                statementPrinterStackPusher.push(node, numberOfTabs, StatementPrinterLocation.END, stack)
                statementPrinterStackPusher.push(node.body, numberOfTabs, StatementPrinterLocation.START, stack)
            }
            else -> {
                val bodyStatementCode = resultStack.pop()
                val expressionCode = codeGenerator.generateCode(node.expressionNode.code)
                val trueLabelCode = labelCodeGenerator.generateLabelCode(node.trueLabel)
                val falseLabelCode = labelCodeGenerator.generateLabelCode(node.falseLabel)
                val doWhileStatementCode = listOf(
                    trueLabelCode,
                    bodyStatementCode,
                    expressionCode,
                    falseLabelCode
                )
                val result = codeGenerator.generateCode(doWhileStatementCode)
                resultStack.push(result)
            }
        }
    }
}