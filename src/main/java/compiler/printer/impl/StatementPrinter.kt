package compiler.printer.impl

import compiler.core.*
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.*
import compiler.printer.impl.internal.IExpressionPrinter
import compiler.printer.impl.internal.IExpressionStatementPrinter
import compiler.printer.impl.internal.IReturnStatementPrinter
import compiler.printer.impl.internal.IStatementPrinter
import compiler.printer.impl.internal.IStatementPrinterStackItemGenerator
import compiler.printer.impl.internal.IVariableDeclarationListPrinter

internal class StatementPrinter(
    private val statementPrinterStackItemGenerator: IStatementPrinterStackItemGenerator,
    private val variableDeclarationListPrinter: IVariableDeclarationListPrinter,
    private val returnStatementPrinter: IReturnStatementPrinter,
    private val expressionStatementPrinter: IExpressionStatementPrinter,
    private val expressionPrinter: IExpressionPrinter
): IStatementPrinter {
    override fun printParsedNode(node: IParsedStatementNode, numberOfTabs: Int): String {
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        stack.push(StatementPrinterStackItem(node, numberOfTabs, 1))

        while(stack.isNotEmpty()) {
            val top = stack.pop()
            val node = top.node

            when(top.location) {
                PrinterConstants.LOCATION_1 -> {
                    val stackItems = statementPrinterStackItemGenerator.generateStatementPrinterStackItems(
                        top.node,
                        top.numberOfTabs
                    )
                    stackItems.forEach { stack.push(it) }
                }
                PrinterConstants.LOCATION_2 -> {
                    when (node) {
                        is ParsedBasicBlockNode -> {
                            val statementStrings = mutableListOf<String>()
                            for(i in 0 until node.statements.size) {
                                statementStrings.add(resultStack.pop())
                            }
                            var tabs = PrinterConstants.EMPTY
                            for(i in 0 until top.numberOfTabs + 1) {
                                tabs += PrinterConstants.TAB
                            }
                            var closingTabs = PrinterConstants.EMPTY
                            for (i in 0 until top.numberOfTabs) {
                                closingTabs += PrinterConstants.TAB
                            }
                            val tabbedStatementStrings = statementStrings.joinToString(PrinterConstants.NEW_LINE + tabs,  PrinterConstants.NEW_LINE + tabs,  PrinterConstants.NEW_LINE + closingTabs)
                            val result = PrinterConstants.LEFT_BRACE +
                                    tabbedStatementStrings +
                                    PrinterConstants.RIGHT_BRACE
                            resultStack.push(result)
                        }
                        is ParsedDoWhileNode -> {
                            val bodyString = resultStack.pop()
                            val expressionString = expressionPrinter.printParsedNode(node.expression)
                            val result =  PrinterConstants.DO +
                                    PrinterConstants.SPACE +
                                    bodyString +
                                    PrinterConstants.SPACE +
                                    PrinterConstants.WHILE +
                                    PrinterConstants.SPACE +
                                    expressionString +
                                    PrinterConstants.SEMICOLON
                            resultStack.push(result)
                        }
                        is ParsedWhileNode -> {
                            val bodyString = resultStack.pop()
                            val expressionString = expressionPrinter.printParsedNode(node.expression)
                            val result = PrinterConstants.WHILE +
                                    PrinterConstants.SPACE +
                                    expressionString +
                                    PrinterConstants.SPACE +
                                    bodyString
                            resultStack.push(result)
                        }
                        is ParsedForNode -> {
                            val bodyString = resultStack.pop()
                            val initExpressionString = expressionPrinter.printParsedNode(node.initExpression)
                            val testExpressionString = expressionPrinter.printParsedNode(node.testExpression)
                            val incrementExpressionString = expressionPrinter.printParsedNode(node.incrementExpression)
                            val result =  PrinterConstants.FOR +
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
                        is ParsedIfNode -> {
                            val ifBodyString = resultStack.pop()
                            val booleanExpressionString = expressionPrinter.printParsedNode(node.booleanExpression)
                            val result = PrinterConstants.IF +
                                    booleanExpressionString +
                                    PrinterConstants.SPACE +
                                    ifBodyString
                            resultStack.push(result)
                        }
                        is ParsedElseNode -> {
                            val elseBodyString = resultStack.pop()
                            val result =PrinterConstants.ELSE + PrinterConstants.SPACE + elseBodyString
                            resultStack.push(result)
                        }
                        is ParsedVariableDeclarationListNode -> {
                            resultStack.push(variableDeclarationListPrinter.printParsedNode(node))
                        }
                        is ParsedReturnNode -> {
                            resultStack.push(returnStatementPrinter.printParsedNode(node))
                        }
                        is ParsedExpressionStatementNode -> {
                            resultStack.push(expressionStatementPrinter.printParsedNode(node))
                        }
                    }
                }
            }
        }

        return resultStack.pop()
    }
}