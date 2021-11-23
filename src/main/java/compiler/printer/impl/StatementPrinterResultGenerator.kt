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
        node: IStatementNode,
        numberOfTabs: Int,
        statementStrings: List<String>
    ): String {
        return when (node) {
            is IBasicBlockNode -> {
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
                val expressionString = expressionPrinter.printNode(node.expression)
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
                val expressionString = expressionPrinter.printNode(node.expression)
                PrinterConstants.WHILE +
                    PrinterConstants.SPACE +
                    expressionString +
                    PrinterConstants.SPACE +
                    bodyString
            }
            is ParsedForNode -> {
                val bodyString = statementStrings[0]
                val initExpressionString = expressionPrinter.printNode(node.initExpression)
                val testExpressionString = expressionPrinter.printNode(node.testExpression)
                val incrementExpressionString = expressionPrinter.printNode(node.incrementExpression)
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
                val booleanExpressionString = expressionPrinter.printNode(node.booleanExpression)
                PrinterConstants.IF +
                    booleanExpressionString +
                    PrinterConstants.SPACE +
                    ifBodyString
            }
            is ParsedElseNode -> {
                val elseBodyString = statementStrings[0]
                PrinterConstants.ELSE + PrinterConstants.SPACE + elseBodyString
            }
            is VariableDeclarationListNode -> {
                variableDeclarationListPrinter.printNode(node)
            }
            is ParsedReturnNode -> {
                returnStatementPrinter.printParsedNode(node)
            }
            is ParsedExpressionStatementNode -> {
                expressionStatementPrinter.printParsedNode(node)
            }
            is TranslatedExpressionStatementNode -> {
                var tabs = PrinterConstants.EMPTY
                for(i in 0 until numberOfTabs + 1) {
                    tabs += PrinterConstants.TAB
                }
                node.expression.code.joinToString(PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + tabs)
            }
            is TranslatedReturnNode -> {
                var tabs = PrinterConstants.EMPTY
                for(i in 0 until numberOfTabs + 1) {
                    tabs += PrinterConstants.TAB
                }
                val expressionCode = node.expressionStatement.expression.code.joinToString(PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + tabs)
                expressionCode + PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + tabs + PrinterConstants.RETURN + PrinterConstants.SPACE + node.expressionStatement.expression.address + PrinterConstants.SEMICOLON
            }
            else -> {
                PrinterConstants.EMPTY
            }
        }
    }
}