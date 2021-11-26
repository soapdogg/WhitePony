package compiler.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.*
import compiler.core.nodes.parsed.*
import compiler.core.nodes.translated.*
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
        stack.push(StatementPrinterStackItem(node, numberOfTabs, PrinterConstants.LOCATION_1))

        while(stack.isNotEmpty()) {
            val top = stack.pop()

            when(top.location) {
                PrinterConstants.LOCATION_1 -> {
                    val returnStackItem = StatementPrinterStackItem(top.node, top.numberOfTabs, PrinterConstants.LOCATION_2)
                    val stackItems = mutableListOf(returnStackItem)
                    when(top.node) {
                        is ParsedBasicBlockNode -> {
                            top.node.statements.forEach {
                                val stackItem =
                                    StatementPrinterStackItem(it, top.numberOfTabs + 1, PrinterConstants.LOCATION_1)
                                stackItems.add(stackItem)
                            }
                        }
                        is TranslatedBasicBlockNode -> {
                            top.node.statements.forEach {
                                val stackItem = StatementPrinterStackItem(it, top.numberOfTabs, PrinterConstants.LOCATION_1)
                                stackItems.add(stackItem)
                            }
                        }
                        is TranslatedForNode -> {
                            top.node.body.forEach {
                                val stackItem = StatementPrinterStackItem(it, top.numberOfTabs, PrinterConstants.LOCATION_1)
                                stackItems.add(stackItem)
                            }
                        }
                        is TranslatedDoWhileNode -> {
                            top.node.body.forEach {
                                val stackItem = StatementPrinterStackItem(it, top.numberOfTabs, PrinterConstants.LOCATION_1)
                                stackItems.add(stackItem)
                            }
                        }
                        is TranslatedWhileNode -> {
                            top.node.body.forEach {
                                val stackItem = StatementPrinterStackItem(it, top.numberOfTabs, PrinterConstants.LOCATION_1)
                                stackItems.add(stackItem)
                            }
                        }
                        is TranslatedIfNode -> {
                            top.node.ifBody.forEach {
                                val stackItem = StatementPrinterStackItem(it, top.numberOfTabs, PrinterConstants.LOCATION_1)
                                stackItems.add(stackItem)
                            }
                            top.node.elseBody.forEach {
                                val stackItem = StatementPrinterStackItem(it, top.numberOfTabs, PrinterConstants.LOCATION_1)
                                stackItems.add(stackItem)
                            }
                        }
                        is ParsedDoWhileNode -> {
                            val stackItem =
                                StatementPrinterStackItem(top.node.body, top.numberOfTabs, PrinterConstants.LOCATION_1)
                            stackItems.add(stackItem)
                        }
                        is ParsedWhileNode -> {
                            val stackItem =
                                StatementPrinterStackItem(top.node.body, top.numberOfTabs, PrinterConstants.LOCATION_1)
                            stackItems.add(stackItem)
                        }
                        is ParsedForNode -> {
                            val stackItem =
                                StatementPrinterStackItem(top.node.body, top.numberOfTabs, PrinterConstants.LOCATION_1)
                            stackItems.add(stackItem)
                        }
                        is ParsedIfNode -> {
                            val stackItem =
                                StatementPrinterStackItem(top.node.ifBody, top.numberOfTabs, PrinterConstants.LOCATION_1)
                            stackItems.add(stackItem)
                            if (top.node.elseBody != null) {
                                val stackItem2 = StatementPrinterStackItem(
                                    top.node.elseBody,
                                    top.numberOfTabs,
                                    PrinterConstants.LOCATION_1
                                )
                                stackItems.add(stackItem2)
                            }
                        }
                    }
                    stackItems.forEach { stack.push(it) }
                }
                PrinterConstants.LOCATION_2 -> {

                    val result = when (top.node) {
                        is IBasicBlockNode -> {
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
                            var tabs = PrinterConstants.EMPTY
                            for (i in 0 until numberOfTabs + 1) {
                                tabs += PrinterConstants.TAB
                            }
                            val bodyStatementStrings = mutableListOf<String>()
                            for (i in 0 until top.node.body.size) {
                                bodyStatementStrings.add(resultStack.pop())
                            }
                            bodyStatementStrings.reverse()
                            top.node.initExpression.code.joinToString(
                                PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + tabs,
                                PrinterConstants.EMPTY,
                                PrinterConstants.SEMICOLON
                            ) +
                                    PrinterConstants.NEW_LINE + tabs + top.node.beginLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON +
                                    top.node.testExpression.code.joinToString(
                                        PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + tabs,
                                        PrinterConstants.NEW_LINE + tabs,
                                        PrinterConstants.SEMICOLON
                                    ) +
                                    PrinterConstants.NEW_LINE + tabs + top.node.trueLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON +
                                    bodyStatementStrings.joinToString(
                                        PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + tabs,
                                        PrinterConstants.NEW_LINE + tabs,
                                        PrinterConstants.NEW_LINE + tabs
                                    ) +
                                    top.node.incrementExpression.code.joinToString(
                                        PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + tabs,
                                        PrinterConstants.EMPTY,
                                        PrinterConstants.SEMICOLON
                                    ) +
                                    PrinterConstants.NEW_LINE + tabs + PrinterConstants.GOTO + PrinterConstants.SPACE + top.node.beginLabel + PrinterConstants.SEMICOLON +
                                    PrinterConstants.NEW_LINE + tabs + top.node.falseLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON
                        }
                        is TranslatedDoWhileNode -> {
                            var tabs = PrinterConstants.EMPTY
                            for (i in 0 until numberOfTabs + 1) {
                                tabs += PrinterConstants.TAB
                            }
                            val bodyStatementStrings = mutableListOf<String>()
                            for (i in 0 until top.node.body.size) {
                                bodyStatementStrings.add(resultStack.pop())
                            }
                            bodyStatementStrings.reverse()
                            top.node.trueLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON +
                                    bodyStatementStrings.joinToString(
                                        PrinterConstants.NEW_LINE + tabs,
                                        PrinterConstants.NEW_LINE + tabs,
                                        PrinterConstants.EMPTY
                                    ) +
                                    top.node.expressionNode.code.joinToString(
                                        PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + tabs,
                                        PrinterConstants.NEW_LINE + tabs,
                                        PrinterConstants.SEMICOLON
                                    ) +
                                    PrinterConstants.NEW_LINE + tabs + top.node.falseLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON

                        }
                        is TranslatedWhileNode -> {
                            var tabs = PrinterConstants.EMPTY
                            for (i in 0 until numberOfTabs + 1) {
                                tabs += PrinterConstants.TAB
                            }
                            val bodyStatementStrings = mutableListOf<String>()
                            for (i in 0 until top.node.body.size) {
                                bodyStatementStrings.add(resultStack.pop())
                            }
                            bodyStatementStrings.reverse()
                            top.node.beginLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON +
                                    top.node.expression.code.joinToString(
                                        PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + tabs,
                                        PrinterConstants.NEW_LINE + tabs,
                                        PrinterConstants.SEMICOLON
                                    ) +
                                    PrinterConstants.NEW_LINE + tabs + top.node.trueLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON +
                                    bodyStatementStrings.joinToString(
                                        PrinterConstants.NEW_LINE + tabs,
                                        PrinterConstants.NEW_LINE + tabs,
                                        PrinterConstants.EMPTY
                                    ) +
                                    PrinterConstants.NEW_LINE + tabs + PrinterConstants.GOTO + PrinterConstants.SPACE + top.node.beginLabel + PrinterConstants.SEMICOLON +
                                    PrinterConstants.NEW_LINE + tabs + top.node.falseLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON
                        }
                        is TranslatedIfNode -> {
                            var tabs = PrinterConstants.EMPTY
                            for (i in 0 until numberOfTabs + 1) {
                                tabs += PrinterConstants.TAB
                            }
                            if (top.node.elseBody.isEmpty()) {
                                val ifBodyStatementString = mutableListOf<String>()
                                for (i in 0 until top.node.ifBody.size) {
                                    ifBodyStatementString.add(resultStack.pop())
                                }
                                ifBodyStatementString.reverse()
                                top.node.expression.code.joinToString(
                                    PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + tabs,
                                    PrinterConstants.EMPTY,
                                    PrinterConstants.SEMICOLON
                                ) +
                                        PrinterConstants.NEW_LINE + tabs + top.node.trueLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON +
                                        ifBodyStatementString.joinToString(
                                            PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + tabs,
                                            PrinterConstants.NEW_LINE + tabs,
                                        ) +
                                        PrinterConstants.NEW_LINE + tabs + top.node.falseLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON
                            } else {
                                val elseBodyStatementString = mutableListOf<String>()
                                for (i in 0 until top.node.elseBody.size) {
                                    elseBodyStatementString.add(resultStack.pop())
                                }
                                elseBodyStatementString.reverse()
                                val ifBodyStatementString = mutableListOf<String>()
                                for (i in 0 until top.node.ifBody.size) {
                                    ifBodyStatementString.add(resultStack.pop())
                                }
                                ifBodyStatementString.reverse()

                                top.node.expression.code.joinToString(
                                    PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + tabs,
                                    PrinterConstants.EMPTY,
                                    PrinterConstants.SEMICOLON
                                ) +
                                        PrinterConstants.NEW_LINE + tabs + top.node.trueLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON +
                                        ifBodyStatementString.joinToString(
                                            PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + tabs,
                                            PrinterConstants.NEW_LINE + tabs,
                                        ) +
                                        PrinterConstants.NEW_LINE + tabs + PrinterConstants.GOTO + PrinterConstants.SPACE + top.node.nextLabel + PrinterConstants.SEMICOLON +
                                        PrinterConstants.NEW_LINE + tabs + top.node.falseLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON +
                                        elseBodyStatementString.joinToString(
                                            PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + tabs,
                                            PrinterConstants.NEW_LINE + tabs,
                                        ) +
                                        PrinterConstants.NEW_LINE + tabs + top.node.nextLabel + PrinterConstants.COLON + PrinterConstants.SPACE + PrinterConstants.SEMICOLON

                            }
                        }
                        is TranslatedExpressionStatementNode -> {
                            var tabs = PrinterConstants.EMPTY
                            for (i in 0 until numberOfTabs + 1) {
                                tabs += PrinterConstants.TAB
                            }
                            top.node.expression.code.joinToString(
                                PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + tabs,
                                PrinterConstants.EMPTY,
                                PrinterConstants.SEMICOLON
                            )
                        }
                        is TranslatedReturnNode -> {
                            var tabs = PrinterConstants.EMPTY
                            for (i in 0 until numberOfTabs + 1) {
                                tabs += PrinterConstants.TAB
                            }
                            val expressionCode =
                                top.node.expressionStatement.expression.code.joinToString(PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + tabs)
                            expressionCode + PrinterConstants.SEMICOLON + PrinterConstants.NEW_LINE + tabs + PrinterConstants.RETURN + PrinterConstants.SPACE + top.node.expressionStatement.expression.address + PrinterConstants.SEMICOLON
                        }
                        else -> {
                            PrinterConstants.EMPTY
                        }
                    }

                    resultStack.push(result)
                }
            }
        }

        return resultStack.pop()
    }
}