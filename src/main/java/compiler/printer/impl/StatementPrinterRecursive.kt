package compiler.printer.impl

import compiler.core.*
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IExpressionPrinter
import compiler.printer.impl.internal.IExpressionStatementPrinter
import compiler.printer.impl.internal.IReturnStatementPrinter
import compiler.printer.impl.internal.IStatementPrinter
import compiler.printer.impl.internal.IVariableDeclarationListPrinter

internal class StatementPrinterRecursive(
    private val variableDeclarationListPrinter: IVariableDeclarationListPrinter,
    private val returnStatementPrinter: IReturnStatementPrinter,
    private val expressionStatementPrinter: IExpressionStatementPrinter,
    private val expressionPrinter: IExpressionPrinter
): IStatementPrinter {
    override fun printParsedNode(node: IParsedStatementNode, numberOfTabs: Int): String {
         return when (node) {
             is ParsedBasicBlockNode -> {
                 val statementStrings = node.statements.map {
                     printParsedNode(it, numberOfTabs + 1)
                 }
                 var tabs = PrinterConstants.EMPTY
                 for(i in 0 until numberOfTabs + 1) {
                     tabs += PrinterConstants.TAB
                 }
                 var closingTabs = PrinterConstants.EMPTY
                 for (i in 0 until numberOfTabs) {
                     closingTabs += PrinterConstants.TAB
                 }

                 val tabbedStatementStrings =statementStrings.joinToString(PrinterConstants.NEW_LINE + tabs,  PrinterConstants.NEW_LINE + tabs,  PrinterConstants.NEW_LINE + closingTabs)
                 return PrinterConstants.LEFT_BRACE +
                         tabbedStatementStrings +
                         PrinterConstants.RIGHT_BRACE
             }
             is ParsedDoWhileNode -> {
                 val bodyString = printParsedNode(node.body, numberOfTabs)
                 val expressionString = expressionPrinter.printParsedNode(node.expression)
                 return PrinterConstants.DO +
                         PrinterConstants.SPACE +
                         bodyString +
                         PrinterConstants.SPACE +
                         PrinterConstants.WHILE +
                         PrinterConstants.SPACE +
                         expressionString +
                         PrinterConstants.SEMICOLON
             }
             is ParsedWhileNode -> {
                 val bodyString = printParsedNode(node.body, numberOfTabs)
                 val expressionString = expressionPrinter.printParsedNode(node.expression)
                 return PrinterConstants.WHILE +
                         PrinterConstants.SPACE +
                         expressionString +
                         PrinterConstants.SPACE +
                         bodyString
             }
             is ParsedForNode -> {
                 val bodyString = printParsedNode(node.body, numberOfTabs)
                 val initExpressionString = expressionPrinter.printParsedNode(node.initExpression)
                 val testExpressionString = expressionPrinter.printParsedNode(node.testExpression)
                 val incrementExpressionString = expressionPrinter.printParsedNode(node.incrementExpression)
                 return PrinterConstants.FOR +
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
                 val ifBodyString = printParsedNode(node.ifBody, numberOfTabs)
                 val booleanExpressionString = expressionPrinter.printParsedNode(node.booleanExpression)
                 return PrinterConstants.IF +
                         booleanExpressionString +
                         PrinterConstants.SPACE +
                         ifBodyString
             }
             is ParsedElseNode -> {
                 val elseBodyString = printParsedNode(node.elseBody, numberOfTabs)
                 return PrinterConstants.ELSE + PrinterConstants.SPACE + elseBodyString
             }
             is ParsedVariableDeclarationListNode -> {
                 return variableDeclarationListPrinter.printParsedNode(node)
             }
             is ParsedReturnNode -> {
                 return returnStatementPrinter.printParsedNode(node)
             }
             is ParsedExpressionStatementNode -> {
                 return expressionStatementPrinter.printParsedNode(node)
             }
             else -> {
                 node.toString()
             }
         }
    }
}