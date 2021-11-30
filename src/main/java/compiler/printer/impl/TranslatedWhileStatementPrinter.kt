package compiler.printer.impl

import compiler.core.nodes.IStatementNode
import compiler.core.nodes.translated.TranslatedWhileNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.printer.impl.internal.ICodeGenerator
import compiler.printer.impl.internal.IGotoCodeGenerator
import compiler.printer.impl.internal.ILabelCodeGenerator
import compiler.printer.impl.internal.IStatementPrinter
import compiler.printer.impl.internal.IStatementPrinterStackPusher

internal class TranslatedWhileStatementPrinter(
    private val statementPrinterStackPusher: IStatementPrinterStackPusher,
    private val codeGenerator: ICodeGenerator,
    private val labelCodeGenerator: ILabelCodeGenerator,
    private val gotoCodeGenerator: IGotoCodeGenerator
): IStatementPrinter {
    override fun printNode(
        node: IStatementNode,
        numberOfTabs: Int,
        location: StatementPrinterLocation,
        stack: Stack<StatementPrinterStackItem>,
        resultStack: Stack<String>,
        appendSemicolon: Boolean
    ) {
        node as TranslatedWhileNode
        when(location) {
            StatementPrinterLocation.START -> {
                statementPrinterStackPusher.push(node, numberOfTabs, StatementPrinterLocation.END, stack)
                statementPrinterStackPusher.push(node.body, numberOfTabs, StatementPrinterLocation.START, stack)
            }
            else -> {

                val bodyStatementCode = resultStack.pop()
                val expressionCode = codeGenerator.generateCode(node.expression.code)
                val beginLabelCode = labelCodeGenerator.generateLabelCode(node.beginLabel)
                val trueLabelCode = labelCodeGenerator.generateLabelCode(node.trueLabel)
                val falseLabelCode = labelCodeGenerator.generateLabelCode(node.falseLabel)
                val gotoBeginCode = gotoCodeGenerator.generateGotoCode(node.beginLabel)
                val whileStatementCode = listOf(
                    beginLabelCode,
                    expressionCode,
                    trueLabelCode,
                    bodyStatementCode,
                    gotoBeginCode,
                    falseLabelCode
                )
                val result = codeGenerator.generateCode(whileStatementCode)
                resultStack.push(result)
            }
        }
    }
}