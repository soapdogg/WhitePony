package compiler.printer.impl

import compiler.core.nodes.IStatementNode
import compiler.core.nodes.translated.TranslatedIfNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.printer.impl.internal.ICodeGenerator
import compiler.printer.impl.internal.IGotoCodeGenerator
import compiler.printer.impl.internal.ILabelCodeGenerator
import compiler.printer.impl.internal.IStatementPrinter
import compiler.printer.impl.internal.IStatementPrinterStackPusher

internal class TranslatedIfStatementPrinter(
    private val statementPrinterStackPusher: IStatementPrinterStackPusher,
    private val codeGenerator: ICodeGenerator,
    private val labelCodeGenerator: ILabelCodeGenerator,
    private val gotoCodeGenerator: IGotoCodeGenerator
) : IStatementPrinter {
    override fun printNode(
        node: IStatementNode,
        numberOfTabs: Int,
        location: StatementPrinterLocation,
        stack: Stack<StatementPrinterStackItem>,
        resultStack: Stack<String>,
        appendSemicolon: Boolean
    ) {
        node as TranslatedIfNode
        when (location) {
            StatementPrinterLocation.START -> {
                statementPrinterStackPusher.push(node, numberOfTabs, StatementPrinterLocation.END, stack)
                statementPrinterStackPusher.push(node.ifBody, numberOfTabs, StatementPrinterLocation.START, stack)
                if (node.elseBody != null) {
                    statementPrinterStackPusher.push(node.elseBody, numberOfTabs, StatementPrinterLocation.START, stack)
                }
            }
            else -> {
                val ifBodyCode = resultStack.pop()
                val expressionCode = codeGenerator.generateCode(node.expression.code)
                val trueLabelCode = labelCodeGenerator.generateLabelCode(node.trueLabel)
                val falseLabelCode = labelCodeGenerator.generateLabelCode(node.falseLabel)

                if (node.elseBody == null) {
                    val ifStatementCode = listOf(
                        expressionCode,
                        trueLabelCode,
                        ifBodyCode,
                        falseLabelCode
                    )
                    val result = codeGenerator.generateCode(ifStatementCode)
                    resultStack.push(result)
                } else {
                    val elseBodyCode = resultStack.pop()
                    val nextLabelCode = labelCodeGenerator.generateLabelCode(node.nextLabel)
                    val gotoNextCode = gotoCodeGenerator.generateGotoCode(node.nextLabel)

                    val ifStatementCode = listOf(
                        expressionCode,
                        trueLabelCode,
                        ifBodyCode,
                        gotoNextCode,
                        falseLabelCode,
                        elseBodyCode,
                        nextLabelCode
                    )

                    val result = codeGenerator.generateCode(ifStatementCode)
                    resultStack.push(result)
                }
            }
        }
    }
}