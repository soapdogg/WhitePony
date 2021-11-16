package compiler.printer.impl

import compiler.core.*
import compiler.printer.impl.internal.*
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
                 var tabs = ""
                 for(i in 0 until numberOfTabs + 1) {
                     tabs += "    "
                 }
                 val statements = node.statements.map {
                     tabs + printParsedNode(it, numberOfTabs + 1)
                 }
                 var closingTabs = ""
                 for (i in 0 until numberOfTabs) {
                     closingTabs += "    "
                 }
                 return "{" + statements.joinToString("\n", "\n",  "\n") + closingTabs + "}"
             }
             is ParsedDoWhileNode -> {
                 return "do " + printParsedNode(node.body, numberOfTabs) + " while " + expressionPrinter.printParsedNode(node.expression) + ";"
             }
             is ParsedWhileNode -> {
                 return "while " + expressionPrinter.printParsedNode(node.expression) + " " + printParsedNode(node.body, numberOfTabs)
             }
             is ParsedForNode -> {
                 return "for (" + expressionPrinter.printParsedNode(node.initExpression) + "; " + expressionPrinter.printParsedNode(node.testExpression) + "; " + expressionPrinter.printParsedNode(node.incrementExpression) + ") " + printParsedNode(node.body, numberOfTabs)
             }
             is ParsedIfNode -> {
                 return "if" + expressionPrinter.printParsedNode(node.booleanExpression) + " " + printParsedNode(node.ifBody, numberOfTabs)
             }
             is ParsedElseNode -> {
                 return "else " + printParsedNode(node.elseBody, numberOfTabs)
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