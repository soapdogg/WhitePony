package compiler.printer.impl

import compiler.core.*
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IExpressionPrinter
import compiler.printer.impl.internal.IExpressionStatementPrinter
import compiler.printer.impl.internal.IReturnStatementPrinter
import compiler.printer.impl.internal.IStatementPrinterResultGenerator
import compiler.printer.impl.internal.IVariableDeclarationListPrinter

internal class StatementPrinterResultGenerator(
    private val variableDeclarationListPrinter: IVariableDeclarationListPrinter,
    private val returnStatementPrinter: IReturnStatementPrinter,
    private val expressionStatementPrinter: IExpressionStatementPrinter,
    private val expressionPrinter: IExpressionPrinter
): IStatementPrinterResultGenerator {
    override fun generateResult(
        node: IParsedStatementNode,
        numberOfTabs: Int,
        statementStrings: List<String>
    ): String {
        return when (node) {
            is ParsedBasicBlockNode -> {
                var tabs = PrinterConstants.EMPTY
                for(i in 0 until numberOfTabs + 1) {
                    tabs += PrinterConstants.TAB
                }
                var closingTabs = PrinterConstants.EMPTY
                for (i in 0 until numberOfTabs) {
                    closingTabs += PrinterConstants.TAB
                }
                val tabbedStatementStrings = statementStrings.joinToString(PrinterConstants.NEW_LINE + tabs,  PrinterConstants.NEW_LINE + tabs,  PrinterConstants.NEW_LINE + closingTabs)
                PrinterConstants.LEFT_BRACE +
                    tabbedStatementStrings +
                    PrinterConstants.RIGHT_BRACE
            }
            is ParsedDoWhileNode -> {
                val bodyString = statementStrings[0]
                val expressionString = expressionPrinter.printParsedNode(node.expression)
                PrinterConstants.DO +
                    PrinterConstants.SPACE +
                    bodyString +
                    PrinterConstants.SPACE +
                    PrinterConstants.WHILE +
                    PrinterConstants.SPACE +
                    expressionString +
                    PrinterConstants.SEMICOLON
            }
            is ParsedWhileNode -> {
                val bodyString = statementStrings[0]
                val expressionString = expressionPrinter.printParsedNode(node.expression)
                PrinterConstants.WHILE +
                    PrinterConstants.SPACE +
                    expressionString +
                    PrinterConstants.SPACE +
                    bodyString
            }
            is ParsedForNode -> {
                val bodyString = statementStrings[0]
                val initExpressionString = expressionPrinter.printParsedNode(node.initExpression)
                val testExpressionString = expressionPrinter.printParsedNode(node.testExpression)
                val incrementExpressionString = expressionPrinter.printParsedNode(node.incrementExpression)
                PrinterConstants.FOR +
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
            }
            is ParsedIfNode -> {
                val ifBodyString = statementStrings[0]
                val booleanExpressionString = expressionPrinter.printParsedNode(node.booleanExpression)
                PrinterConstants.IF +
                    booleanExpressionString +
                    PrinterConstants.SPACE +
                    ifBodyString
            }
            is ParsedElseNode -> {
                val elseBodyString = statementStrings[0]
                PrinterConstants.ELSE + PrinterConstants.SPACE + elseBodyString
            }
            is ParsedVariableDeclarationListNode -> {
                variableDeclarationListPrinter.printParsedNode(node)
            }
            is ParsedReturnNode -> {
                returnStatementPrinter.printParsedNode(node)
            }
            is ParsedExpressionStatementNode -> {
                expressionStatementPrinter.printParsedNode(node)
            }
            else -> {
                PrinterConstants.EMPTY
            }
        }
    }
}