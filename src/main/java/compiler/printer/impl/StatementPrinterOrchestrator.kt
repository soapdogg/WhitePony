package compiler.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.*
import compiler.core.nodes.parsed.*
import compiler.core.nodes.translated.*
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.printer.impl.internal.ICodeGenerator
import compiler.printer.impl.internal.IExpressionPrinter
import compiler.printer.impl.internal.IExpressionStatementPrinter
import compiler.printer.impl.internal.IGotoCodeGenerator
import compiler.printer.impl.internal.ILabelCodeGenerator
import compiler.printer.impl.internal.IReturnStatementPrinter
import compiler.printer.impl.internal.IStatementPrinter
import compiler.printer.impl.internal.IStatementPrinterOrchestrator

internal class StatementPrinterOrchestrator(
    private val printerMap: Map<Class<out IStatementNode>, IStatementPrinter>,
    private val returnStatementPrinter: IReturnStatementPrinter,
    private val expressionStatementPrinter: IExpressionStatementPrinter,
    private val expressionPrinter: IExpressionPrinter,
    private val codeGenerator: ICodeGenerator,
    private val labelCodeGenerator: ILabelCodeGenerator,
    private val gotoCodeGenerator: IGotoCodeGenerator
): IStatementPrinterOrchestrator {
    override fun printNode(node: IStatementNode, numberOfTabs: Int, appendSemicolon: Boolean): String {
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        stack.push(StatementPrinterStackItem(node, numberOfTabs, StatementPrinterLocation.START))
        val wrapInBraces = node is TranslatedBasicBlockNode

        while(stack.isNotEmpty()) {
            val top = stack.pop()

            if (printerMap.containsKey(top.node.javaClass)) {
                val printer = printerMap.getValue(top.node.javaClass)
                printer.printNode(
                    top.node,
                    top.numberOfTabs,
                    top.location,
                    stack,
                    resultStack,
                    appendSemicolon
                )
            } else {

                when (top.location) {
                    StatementPrinterLocation.START -> {
                        val returnStackItem = StatementPrinterStackItem(
                            top.node,
                            top.numberOfTabs,
                            StatementPrinterLocation.END_LOCATION
                        )
                        val stackItems = mutableListOf(returnStackItem)
                        when (top.node) {
                            is ParsedBasicBlockNode -> {
                                top.node.statements.forEach {
                                    val stackItem =
                                        StatementPrinterStackItem(
                                            it,
                                            top.numberOfTabs + 1,
                                            StatementPrinterLocation.START
                                        )
                                    stackItems.add(stackItem)
                                }
                            }
                            is TranslatedBasicBlockNode -> {
                                top.node.statements.forEach {
                                    val stackItem = StatementPrinterStackItem(
                                        it,
                                        top.numberOfTabs,
                                        StatementPrinterLocation.START
                                    )
                                    stackItems.add(stackItem)
                                }
                            }
                            is TranslatedDoWhileNode -> {
                                val stackItem = StatementPrinterStackItem(
                                    top.node.body,
                                    top.numberOfTabs,
                                    StatementPrinterLocation.START
                                )
                                stackItems.add(stackItem)
                            }
                            is TranslatedWhileNode -> {
                                val stackItem = StatementPrinterStackItem(
                                    top.node.body,
                                    top.numberOfTabs,
                                    StatementPrinterLocation.START
                                )
                                stackItems.add(stackItem)
                            }
                            is TranslatedIfNode -> {
                                if (top.node.elseBody != null) {
                                    val stackItem2 = StatementPrinterStackItem(
                                        top.node.elseBody,
                                        top.numberOfTabs,
                                        StatementPrinterLocation.START
                                    )
                                    stackItems.add(stackItem2)
                                }

                                val stackItem = StatementPrinterStackItem(
                                    top.node.ifBody,
                                    top.numberOfTabs,
                                    StatementPrinterLocation.START
                                )
                                stackItems.add(stackItem)
                            }
                            is ParsedIfNode -> {
                                val stackItem =
                                    StatementPrinterStackItem(
                                        top.node.ifBody,
                                        top.numberOfTabs,
                                        StatementPrinterLocation.START
                                    )
                                stackItems.add(stackItem)
                                if (top.node.elseBody != null) {
                                    val stackItem2 = StatementPrinterStackItem(
                                        top.node.elseBody,
                                        top.numberOfTabs,
                                        StatementPrinterLocation.START
                                    )
                                    stackItems.add(stackItem2)
                                }
                            }
                        }
                        stackItems.forEach { stack.push(it) }
                    }
                    StatementPrinterLocation.END_LOCATION -> {

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
                                    PrinterConstants.SEPARATOR,

                                )
                                tabbedStatementStrings
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
                            is ParsedReturnNode -> {
                                returnStatementPrinter.printNode(top.node)
                            }
                            is ParsedExpressionStatementNode -> {
                                expressionStatementPrinter.printNode(top.node)
                            }
                            is TranslatedDoWhileNode -> {
                                val bodyStatementStrings = resultStack.pop()
                                val expressionCode = codeGenerator.generateCode(top.node.expressionNode.code)
                                val trueLabelCode = labelCodeGenerator.generateLabelCode(top.node.trueLabel)
                                val falseLabelCode = labelCodeGenerator.generateLabelCode(top.node.falseLabel)

                                trueLabelCode + PrinterConstants.SEMICOLON +
                                        PrinterConstants.TABBED_NEW_LINE + bodyStatementStrings + PrinterConstants.SEMICOLON +
                                        PrinterConstants.TABBED_NEW_LINE + expressionCode + PrinterConstants.SEMICOLON +
                                        PrinterConstants.TABBED_NEW_LINE + falseLabelCode

                            }
                            is TranslatedWhileNode -> {
                                val bodyStatementStrings = resultStack.pop()
                                val expressionCode = codeGenerator.generateCode(top.node.expression.code)
                                val beginLabelCode = labelCodeGenerator.generateLabelCode(top.node.beginLabel)
                                val trueLabelCode = labelCodeGenerator.generateLabelCode(top.node.trueLabel)
                                val falseLabelCode = labelCodeGenerator.generateLabelCode(top.node.falseLabel)
                                val gotoBeginCode = gotoCodeGenerator.generateGotoCode(top.node.beginLabel)
                                beginLabelCode + PrinterConstants.SEMICOLON +
                                        PrinterConstants.TABBED_NEW_LINE + expressionCode + PrinterConstants.SEMICOLON +
                                        PrinterConstants.TABBED_NEW_LINE + trueLabelCode + PrinterConstants.SEMICOLON +
                                        PrinterConstants.TABBED_NEW_LINE + bodyStatementStrings + PrinterConstants.SEMICOLON +
                                        PrinterConstants.TABBED_NEW_LINE + gotoBeginCode + PrinterConstants.SEMICOLON +
                                        PrinterConstants.TABBED_NEW_LINE + falseLabelCode
                            }
                            is TranslatedIfNode -> {
                                if (top.node.elseBody == null) {
                                    val ifBodyStatementString = resultStack.pop()
                                    val expressionCode = codeGenerator.generateCode(top.node.expression.code)
                                    val trueLabelCode = labelCodeGenerator.generateLabelCode(top.node.trueLabel)
                                    val falseLabelCode = labelCodeGenerator.generateLabelCode(top.node.falseLabel)

                                    expressionCode + PrinterConstants.SEMICOLON +
                                            PrinterConstants.TABBED_NEW_LINE + trueLabelCode + PrinterConstants.SEMICOLON +
                                            PrinterConstants.TABBED_NEW_LINE + ifBodyStatementString + PrinterConstants.SEMICOLON +
                                            PrinterConstants.TABBED_NEW_LINE + falseLabelCode
                                } else {
                                    val elseBodyStatementString = resultStack.pop()
                                    val ifBodyStatementString = resultStack.pop()
                                    val expressionCode = codeGenerator.generateCode(top.node.expression.code)
                                    val trueLabelCode = labelCodeGenerator.generateLabelCode(top.node.trueLabel)
                                    val falseLabelCode = labelCodeGenerator.generateLabelCode(top.node.falseLabel)
                                    val nextLabelCode = labelCodeGenerator.generateLabelCode(top.node.nextLabel)
                                    val gotoNextCode = gotoCodeGenerator.generateGotoCode(top.node.nextLabel)

                                    expressionCode + PrinterConstants.SEMICOLON +
                                            PrinterConstants.TABBED_NEW_LINE + trueLabelCode + PrinterConstants.SEMICOLON +
                                            PrinterConstants.TABBED_NEW_LINE + ifBodyStatementString + PrinterConstants.SEMICOLON +
                                            PrinterConstants.TABBED_NEW_LINE + gotoNextCode + PrinterConstants.SEMICOLON +
                                            PrinterConstants.TABBED_NEW_LINE + falseLabelCode + PrinterConstants.SEMICOLON +
                                            PrinterConstants.TABBED_NEW_LINE + elseBodyStatementString + PrinterConstants.SEMICOLON +
                                            PrinterConstants.TABBED_NEW_LINE + nextLabelCode
                                }
                            }
                            is TranslatedExpressionStatementNode -> {
                                codeGenerator.generateCode(top.node.expression.code)
                            }
                            is TranslatedReturnNode -> {
                                val expressionCode = codeGenerator.generateCode(top.node.expressionStatement.expression.code) + PrinterConstants.SEMICOLON

                                expressionCode +
                                        PrinterConstants.TABBED_NEW_LINE + PrinterConstants.RETURN + PrinterConstants.SPACE + top.node.expressionStatement.expression.address + PrinterConstants.SEMICOLON
                            }
                            else -> {
                                PrinterConstants.EMPTY
                            }
                        }

                        resultStack.push(result)
                    }
                }
            }
        }

        val top = resultStack.pop()
        return if (wrapInBraces) {
            PrinterConstants.LEFT_BRACE + PrinterConstants.TABBED_NEW_LINE + top + PrinterConstants.NEW_LINE + PrinterConstants.RIGHT_BRACE
        } else {
            top
        }
    }
}