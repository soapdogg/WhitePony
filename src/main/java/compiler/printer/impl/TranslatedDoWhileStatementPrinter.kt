package compiler.printer.impl

import compiler.core.nodes.IStatementNode
import compiler.core.nodes.translated.TranslatedDoWhileNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.printer.impl.internal.ICodeGenerator
import compiler.printer.impl.internal.ILabelCodeGenerator
import compiler.printer.impl.internal.IStatementPrinter
import compiler.printer.impl.internal.IStatementPrinterStackPusher

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
                statementPrinterStackPusher.push(node, numberOfTabs, StatementPrinterLocation.END_DO_WHILE, stack)
                statementPrinterStackPusher.push(node.body, numberOfTabs, StatementPrinterLocation.START, stack)
            }
            StatementPrinterLocation.END_DO_WHILE -> {
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