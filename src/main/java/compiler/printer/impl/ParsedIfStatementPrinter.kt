package compiler.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.IStatementNode
import compiler.core.nodes.parsed.ParsedIfNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.printer.impl.internal.IExpressionPrinter
import compiler.printer.impl.internal.IStatementPrinter
import compiler.printer.impl.internal.IStatementPrinterStackPusher
import compiler.printer.impl.internal.ITabsGenerator

internal class ParsedIfStatementPrinter(
    private val statementPrinterStackPusher: IStatementPrinterStackPusher,
    private val expressionPrinter: IExpressionPrinter,
    private val tabsGenerator: ITabsGenerator
): IStatementPrinter {
    override fun printNode(
        node: IStatementNode,
        numberOfTabs: Int,
        location: StatementPrinterLocation,
        stack: Stack<StatementPrinterStackItem>,
        resultStack: Stack<String>,
        appendSemicolon: Boolean
    ) {
        node as ParsedIfNode
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
                val booleanExpressionCode = expressionPrinter.printNode(node.booleanExpression)
                val ifString = PrinterConstants.IF +
                        booleanExpressionCode +
                        PrinterConstants.SPACE +
                        ifBodyCode
                val result = if (node.elseBody != null) {
                    val tabs = tabsGenerator.generateTabs(numberOfTabs)
                    val elseBodyString = resultStack.pop()
                    ifString + PrinterConstants.NEW_LINE + tabs + PrinterConstants.ELSE + PrinterConstants.SPACE + elseBodyString
                } else {
                    ifString
                }
                resultStack.push(result)
            }
        }
    }
}