package compiler.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.IStatementNode
import compiler.core.nodes.parsed.ParsedForNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.printer.impl.internal.IExpressionPrinter
import compiler.printer.impl.internal.IStatementPrinter
import compiler.printer.impl.internal.IStatementPrinterStackPusher

internal class ParsedForStatementPrinter(
    private val statementPrinterStackPusher: IStatementPrinterStackPusher,
    private val expressionPrinter: IExpressionPrinter
): IStatementPrinter {
    override fun printNode(
        node: IStatementNode,
        numberOfTabs: Int,
        location: StatementPrinterLocation,
        stack: Stack<StatementPrinterStackItem>,
        resultStack: Stack<String>,
        appendSemicolon: Boolean
    ) {
        node as ParsedForNode
        when(location) {
            StatementPrinterLocation.START -> {
                statementPrinterStackPusher.push(node, numberOfTabs, StatementPrinterLocation.END_FOR, stack)
                statementPrinterStackPusher.push(node.body, numberOfTabs, StatementPrinterLocation.START, stack)
            }
            StatementPrinterLocation.END_FOR -> {
                val bodyString = resultStack.pop()
                val initExpressionString = expressionPrinter.printNode(node.initExpression)
                val testExpressionString = expressionPrinter.printNode(node.testExpression)
                val incrementExpressionString =
                    expressionPrinter.printNode(node.incrementExpression)
                val result = PrinterConstants.FOR +
                        PrinterConstants.SPACE +
                        PrinterConstants.LEFT_PARENTHESES +
                        initExpressionString +
                        PrinterConstants.SEMICOLON +
                        PrinterConstants.SPACE +
                        testExpressionString +
                        PrinterConstants.SEMICOLON +
                        PrinterConstants.SPACE +
                        incrementExpressionString +
                        PrinterConstants.RIGHT_PARENTHESES +
                        PrinterConstants.SPACE +
                        bodyString
                resultStack.push(result)
            }
        }
    }
}