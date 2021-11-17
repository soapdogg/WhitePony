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
                 var tabs = PrinterConstants.EMPTY
                 for(i in 0 until numberOfTabs + 1) {
                     tabs += PrinterConstants.TAB
                 }
                 val statements = node.statements.map {
                     tabs + printParsedNode(it, numberOfTabs + 1)
                 }
                 var closingTabs = PrinterConstants.EMPTY
                 for (i in 0 until numberOfTabs) {
                     closingTabs += PrinterConstants.TAB
                 }
                 return PrinterConstants.LEFT_BRACE +
                         statements.joinToString(PrinterConstants.NEW_LINE, PrinterConstants.NEW_LINE,  PrinterConstants.NEW_LINE) +
                         closingTabs +
                         PrinterConstants.RIGHT_BRACE
             }
             is ParsedDoWhileNode -> {
                 return PrinterConstants.DO +
                         PrinterConstants.SPACE +
                         printParsedNode(node.body, numberOfTabs) +
                         PrinterConstants.SPACE +
                         PrinterConstants.WHILE +
                         PrinterConstants.SPACE +
                         expressionPrinter.printParsedNode(node.expression) +
                         PrinterConstants.SEMICOLON
             }
             is ParsedWhileNode -> {
                 return PrinterConstants.WHILE +
                         PrinterConstants.SPACE +
                         expressionPrinter.printParsedNode(node.expression) +
                         PrinterConstants.SPACE +
                         printParsedNode(node.body, numberOfTabs)
             }
             is ParsedForNode -> {
                 return PrinterConstants.FOR +
                         PrinterConstants.SPACE +
                         PrinterConstants.LEFT_PARENTHESES +
                         expressionPrinter.printParsedNode(node.initExpression) +
                         PrinterConstants.SEMICOLON +
                         PrinterConstants.SPACE +
                         expressionPrinter.printParsedNode(node.testExpression) +
                         PrinterConstants.SEMICOLON +
                         PrinterConstants.SPACE +
                         expressionPrinter.printParsedNode(node.incrementExpression) +
                         PrinterConstants.RIGHT_PARENTHESES +
                         PrinterConstants.SPACE +
                         printParsedNode(node.body, numberOfTabs)
             }
             is ParsedIfNode -> {
                 return PrinterConstants.IF +
                         expressionPrinter.printParsedNode(node.booleanExpression) +
                         PrinterConstants.SPACE +
                         printParsedNode(node.ifBody, numberOfTabs)
             }
             is ParsedElseNode -> {
                 return PrinterConstants.ELSE + PrinterConstants.SPACE + printParsedNode(node.elseBody, numberOfTabs)
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