package compiler.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.*
import compiler.core.nodes.parsed.*
import compiler.core.nodes.translated.*
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterStackItem
import compiler.printer.impl.internal.IExpressionPrinter
import compiler.printer.impl.internal.IExpressionStatementPrinter
import compiler.printer.impl.internal.IReturnStatementPrinter
import compiler.printer.impl.internal.IStatementPrinter
import compiler.printer.impl.internal.IVariableDeclarationListPrinter

internal class StatementPrinter(
    private val variableDeclarationListPrinter: IVariableDeclarationListPrinter,
    private val returnStatementPrinter: IReturnStatementPrinter,
    private val expressionStatementPrinter: IExpressionStatementPrinter,
    private val expressionPrinter: IExpressionPrinter
): IStatementPrinter {
    override fun printNode(node: IStatementNode, numberOfTabs: Int): String {
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        stack.push(StatementPrinterStackItem(node, numberOfTabs, LocationConstants.LOCATION_1))
        val wrapInBraces = node is TranslatedBasicBlockNode

        while(stack.isNotEmpty()) {
            val top = stack.pop()

            when(top.location) {
                LocationConstants.LOCATION_1 -> {
                    val returnStackItem = StatementPrinterStackItem(top.node, top.numberOfTabs, LocationConstants.LOCATION_2)
                    val stackItems = mutableListOf(returnStackItem)
                    when(top.node) {
                        is ParsedBasicBlockNode -> {
                            top.node.statements.forEach {
                                val stackItem =
                                    StatementPrinterStackItem(it, top.numberOfTabs + 1, LocationConstants.LOCATION_1)
                                stackItems.add(stackItem)
                            }
                        }
                        is TranslatedBasicBlockNode -> {
                            top.node.statements.forEach {
                                val stackItem = StatementPrinterStackItem(it, top.numberOfTabs, LocationConstants.LOCATION_1)
                                stackItems.add(stackItem)
                            }
                        }
                        is TranslatedForNode -> {
                            val stackItem = StatementPrinterStackItem(top.node.body, top.numberOfTabs, LocationConstants.LOCATION_1)
                            stackItems.add(stackItem)
                        }
                        is TranslatedDoWhileNode -> {
                            val stackItem = StatementPrinterStackItem(top.node.body, top.numberOfTabs, LocationConstants.LOCATION_1)
                            stackItems.add(stackItem)
                        }
                        is TranslatedWhileNode -> {
                            val stackItem = StatementPrinterStackItem(top.node.body, top.numberOfTabs, LocationConstants.LOCATION_1)
                            stackItems.add(stackItem)
                        }
                        is TranslatedIfNode -> {
                            if (top.node.elseBody != null) {
                                val stackItem2 = StatementPrinterStackItem(top.node.elseBody, top.numberOfTabs, LocationConstants.LOCATION_1)
                                stackItems.add(stackItem2)
                            }

                            val stackItem = StatementPrinterStackItem(top.node.ifBody, top.numberOfTabs, LocationConstants.LOCATION_1)
                            stackItems.add(stackItem)
                        }
                        is ParsedDoWhileNode -> {
                            val stackItem =
                                StatementPrinterStackItem(top.node.body, top.numberOfTabs, LocationConstants.LOCATION_1)
                            stackItems.add(stackItem)
                        }
                        is ParsedWhileNode -> {
                            val stackItem =
                                StatementPrinterStackItem(top.node.body, top.numberOfTabs, LocationConstants.LOCATION_1)
                            stackItems.add(stackItem)
                        }
                        is ParsedForNode -> {
                            val stackItem =
                                StatementPrinterStackItem(top.node.body, top.numberOfTabs, LocationConstants.LOCATION_1)
                            stackItems.add(stackItem)
                        }
                        is ParsedIfNode -> {
                            val stackItem =
                                StatementPrinterStackItem(top.node.ifBody, top.numberOfTabs, LocationConstants.LOCATION_1)
                            stackItems.add(stackItem)
                            if (top.node.elseBody != null) {
                                val stackItem2 = StatementPrinterStackItem(
                                    top.node.elseBody,
                                    top.numberOfTabs,
                                    LocationConstants.LOCATION_1
                                )
                                stackItems.add(stackItem2)
                            }
                        }
                    }
                    stackItems.forEach { stack.push(it) }
                }
                LocationConstants.LOCATION_2 -> {

                    val result = when (top.node) {
                        is ParsedBasicBlockNode -> {
                            var tabs = PrinterConstants.EMPTY
                            for (i in 0 until top.numberOfTabs + 1) {
                                tabs += PrinterConstants.TAB
                            }
                            var closingTabs = PrinterConstants.EMPTY
                            for (i in 0 until top.numberOfTabs) {
                                closingTabs += PrinterConstants.TAB
                            }
                            val statementStrings = mutableListOf<String>()
                            for (i in 0 until top.node.statements.size) {
                                statementStrings.add(resultStack.pop())
                            }
                            val tabbedStatementStrings = statementStrings.joinToString(
                                PrinterConstants.NEW_LINE + tabs,
                                PrinterConstants.NEW_LINE + tabs,
                                PrinterConstants.NEW_LINE + closingTabs
                            )
                            PrinterConstants.LEFT_BRACE +
                                    tabbedStatementStrings +
                                    PrinterConstants.RIGHT_BRACE
                        }
                        is TranslatedBasicBlockNode -> {
                            val statementStrings = mutableListOf<String>()
                            for (i in 0 until top.node.statements.size) {
                                statementStrings.add(resultStack.pop())
                            }
                            val tabbedStatementStrings = statementStrings.joinToString(
                                PrinterConstants.NEW_LINE + PrinterConstants.TAB,
                            )
                            tabbedStatementStrings
                        }
                        is ParsedDoWhileNode -> {
                            val bodyString = resultStack.pop()
                            val expressionString = expressionPrinter.printNode(top.node.expression)
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
                            val bodyString = resultStack.pop()
                            val expressionString = expressionPrinter.printNode(top.node.expression)
                            PrinterConstants.WHILE +
                                    PrinterConstants.SPACE +
                                    expressionString +
                                    PrinterConstants.SPACE +
                                    bodyString
                        }
                        is ParsedForNode -> {
                            val bodyString = resultStack.pop()
                            val initExpressionString = expressionPrinter.printNode(top.node.initExpression)
                            val testExpressionString = expressionPrinter.printNode(top.node.testExpression)
                            val incrementExpressionString = expressionPrinter.printNode(top.node.incrementExpression)
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
                            val ifBodyString = resultStack.pop()
                            val booleanExpressionString = expressionPrinter.printNode(top.node.booleanExpression)
                            val ifString = PrinterConstants.IF +
                                    booleanExpressionString +
                                    PrinterConstants.SPACE +
                                    ifBodyString
                            if (top.node.elseBody != null) {
                                var tabs = PrinterConstants.EMPTY
                                for (i in 0 until top.numberOfTabs) {
                                    tabs += PrinterConstants.TAB
                                }
                                val elseBodyString = resultStack.pop()
                                ifString + PrinterConstants.NEW_LINE + tabs + PrinterConstants.ELSE + PrinterConstants.SPACE + elseBodyString
                            } else {
                                ifString
                            }
                        }
                        is VariableDeclarationListNode -> {
                            variableDeclarationListPrinter.printNode(top.node)
                        }
                        is ParsedReturnNode -> {
                            returnStatementPrinter.printParsedNode(top.node)
                        }
                        is ParsedExpressionStatementNode -> {
                            expressionStatementPrinter.printParsedNode(top.node)
                        }
                        is TranslatedForNode -> {
                            val bodyStatementStrings = resultStack.pop()
                            top.node.initExpression.code.joinToString(
                                PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + PrinterConstants.TAB,
                                PrinterConstants.EMPTY,
                                PrinterConstants.SEMICOLON
                            ) +
                                    PrinterConstants.NEW_LINE + PrinterConstants.TAB + top.node.beginLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON +
                                    top.node.testExpression.code.joinToString(
                                        PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + PrinterConstants.TAB,
                                        PrinterConstants.NEW_LINE + PrinterConstants.TAB,
                                        PrinterConstants.SEMICOLON
                                    ) +
                                    PrinterConstants.NEW_LINE + PrinterConstants.TAB + top.node.trueLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON +
                                    PrinterConstants.NEW_LINE + PrinterConstants.TAB + bodyStatementStrings +
                                    top.node.incrementExpression.code.joinToString(
                                        PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + PrinterConstants.TAB,
                                        PrinterConstants.NEW_LINE + PrinterConstants.TAB,
                                        PrinterConstants.SEMICOLON
                                    ) +
                                    PrinterConstants.NEW_LINE + PrinterConstants.TAB + PrinterConstants.GOTO + PrinterConstants.SPACE + top.node.beginLabel + PrinterConstants.SEMICOLON +
                                    PrinterConstants.NEW_LINE + PrinterConstants.TAB + top.node.falseLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON
                        }
                        is TranslatedDoWhileNode -> {
                            val bodyStatementStrings = resultStack.pop()

                            top.node.trueLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON +
                                    PrinterConstants.NEW_LINE + PrinterConstants.TAB + bodyStatementStrings +
                                    top.node.expressionNode.code.joinToString(
                                        PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + PrinterConstants.TAB,
                                        PrinterConstants.NEW_LINE + PrinterConstants.TAB,
                                        PrinterConstants.SEMICOLON
                                    ) +
                                    PrinterConstants.NEW_LINE + PrinterConstants.TAB + top.node.falseLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON

                        }
                        is TranslatedWhileNode -> {
                            val bodyStatementStrings = resultStack.pop()
                            top.node.beginLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON +
                                    top.node.expression.code.joinToString(
                                        PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + PrinterConstants.TAB,
                                        PrinterConstants.NEW_LINE + PrinterConstants.TAB,
                                        PrinterConstants.SEMICOLON
                                    ) +
                                    PrinterConstants.NEW_LINE + PrinterConstants.TAB + top.node.trueLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON +
                                    PrinterConstants.NEW_LINE + PrinterConstants.TAB + bodyStatementStrings +
                                    PrinterConstants.NEW_LINE + PrinterConstants.TAB + PrinterConstants.GOTO + PrinterConstants.SPACE + top.node.beginLabel + PrinterConstants.SEMICOLON +
                                    PrinterConstants.NEW_LINE + PrinterConstants.TAB + top.node.falseLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON
                        }
                        is TranslatedIfNode -> {
                            if (top.node.elseBody == null) {
                                val ifBodyStatementString = resultStack.pop()
                                top.node.expression.code.joinToString(
                                    PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + PrinterConstants.TAB,
                                    PrinterConstants.EMPTY,
                                    PrinterConstants.SEMICOLON
                                ) +
                                        PrinterConstants.NEW_LINE + PrinterConstants.TAB + top.node.trueLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON +
                                        PrinterConstants.NEW_LINE + PrinterConstants.TAB + ifBodyStatementString +
                                        PrinterConstants.NEW_LINE + PrinterConstants.TAB + top.node.falseLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON
                            } else {
                                val elseBodyStatementString = resultStack.pop()
                                val ifBodyStatementString = resultStack.pop()
                                top.node.expression.code.joinToString(
                                    PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + PrinterConstants.TAB,
                                    PrinterConstants.EMPTY,
                                    PrinterConstants.SEMICOLON
                                ) +
                                        PrinterConstants.NEW_LINE + PrinterConstants.TAB + top.node.trueLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON +
                                        PrinterConstants.NEW_LINE + PrinterConstants.TAB + ifBodyStatementString +
                                        PrinterConstants.NEW_LINE + PrinterConstants.TAB + PrinterConstants.GOTO + PrinterConstants.SPACE + top.node.nextLabel + PrinterConstants.SEMICOLON +
                                        PrinterConstants.NEW_LINE + PrinterConstants.TAB + top.node.falseLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON +
                                        PrinterConstants.NEW_LINE + PrinterConstants.TAB + elseBodyStatementString +
                                        PrinterConstants.NEW_LINE + PrinterConstants.TAB + top.node.nextLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON

                            }
                        }
                        is TranslatedExpressionStatementNode -> {
                            top.node.expression.code.joinToString(
                                PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + PrinterConstants.TAB,
                                PrinterConstants.EMPTY,
                                PrinterConstants.SEMICOLON
                            )
                        }
                        is TranslatedReturnNode -> {
                            val expressionCode =
                                top.node.expressionStatement.expression.code.joinToString(PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + PrinterConstants.TAB)
                            expressionCode + PrinterConstants.SEMICOLON +
                                    PrinterConstants.NEW_LINE + PrinterConstants.TAB + PrinterConstants.RETURN + PrinterConstants.SPACE + top.node.expressionStatement.expression.address + PrinterConstants.SEMICOLON
                        }
                        else -> {
                            PrinterConstants.EMPTY
                        }
                    }

                    resultStack.push(result)
                }
            }
        }

        val top = resultStack.pop()
        return if (wrapInBraces) {
            PrinterConstants.LEFT_BRACE + PrinterConstants.NEW_LINE + PrinterConstants.TAB + top + PrinterConstants.NEW_LINE + PrinterConstants.RIGHT_BRACE
        } else {
            top
        }
    }
}