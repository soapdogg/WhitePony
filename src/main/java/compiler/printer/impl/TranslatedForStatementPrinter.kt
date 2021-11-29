package compiler.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.IStatementNode
import compiler.core.nodes.translated.TranslatedForNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.printer.impl.internal.ICodeGenerator
import compiler.printer.impl.internal.IGotoCodeGenerator
import compiler.printer.impl.internal.ILabelCodeGenerator
import compiler.printer.impl.internal.IStatementPrinter
import compiler.printer.impl.internal.IStatementPrinterStackPusher

internal class TranslatedForStatementPrinter(
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
        resultStack: Stack<String>
    ) {
        node as TranslatedForNode
        when(location) {
            StatementPrinterLocation.START -> {
                statementPrinterStackPusher.push(node, numberOfTabs, StatementPrinterLocation.END_FOR, stack)
                statementPrinterStackPusher.push(node.body, numberOfTabs, StatementPrinterLocation.START, stack)
            }
            StatementPrinterLocation.END_FOR -> {
                val bodyStatementString = resultStack.pop()
                val initExpressionCode = codeGenerator.generateCode(node.initExpression.code)
                val testExpressionCode = codeGenerator.generateCode(node.testExpression.code)
                val incrementExpressionCode = codeGenerator.generateCode(node.incrementExpression.code)
                val beginLabelCode = labelCodeGenerator.generateLabelCode(node.beginLabel)
                val trueLabelCode = labelCodeGenerator.generateLabelCode(node.trueLabel)
                val falseLabelCode = labelCodeGenerator.generateLabelCode(node.falseLabel)
                val gotoBeginCode = gotoCodeGenerator.generateGotoCode(node.beginLabel)

                val result = initExpressionCode + PrinterConstants.SEMICOLON +
                        PrinterConstants.TABBED_NEW_LINE + beginLabelCode + PrinterConstants.SEMICOLON +
                        PrinterConstants.TABBED_NEW_LINE + testExpressionCode + PrinterConstants.SEMICOLON +
                        PrinterConstants.TABBED_NEW_LINE + trueLabelCode + PrinterConstants.SEMICOLON +
                        PrinterConstants.TABBED_NEW_LINE + bodyStatementString +
                        PrinterConstants.TABBED_NEW_LINE + incrementExpressionCode + PrinterConstants.SEMICOLON +
                        PrinterConstants.TABBED_NEW_LINE + gotoBeginCode + PrinterConstants.SEMICOLON +
                        PrinterConstants.TABBED_NEW_LINE + falseLabelCode + PrinterConstants.SEMICOLON
                resultStack.push(result)
            }
        }
    }
}