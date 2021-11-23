package compiler.printer.impl

import compiler.core.*
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IStatementPrinter
import compiler.printer.impl.internal.IStatementPrinterResultGenerator
import compiler.printer.impl.internal.IStatementPrinterStackItemGenerator

internal class StatementPrinter(
    private val statementPrinterStackItemGenerator: IStatementPrinterStackItemGenerator,
    private val statementPrinterResultGenerator: IStatementPrinterResultGenerator
): IStatementPrinter {
    override fun printNode(node: IStatementNode, numberOfTabs: Int): String {
        return if (node is IParsedStatementNode) {
            printParseNode(node, numberOfTabs)
        } else {
            PrinterConstants.EMPTY
        }
    }

    private fun printParseNode(node: IParsedStatementNode, numberOfTabs: Int): String {
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        stack.push(StatementPrinterStackItem(node, numberOfTabs, PrinterConstants.LOCATION_1))

        while(stack.isNotEmpty()) {
            val top = stack.pop()

            when(top.location) {
                PrinterConstants.LOCATION_1 -> {
                    val stackItems = statementPrinterStackItemGenerator.generateStatementPrinterStackItems(
                        top.node,
                        top.numberOfTabs
                    )
                    stackItems.forEach { stack.push(it) }
                }
                PrinterConstants.LOCATION_2 -> {
                    val statementStrings = mutableListOf<String>()
                    for(i in 0 until top.node.getNumberOfStatements()) {
                        statementStrings.add(resultStack.pop())
                    }

                    val result = statementPrinterResultGenerator.generateResult(top.node, top.numberOfTabs, statementStrings)
                    resultStack.push(result)
                }
            }
        }

        return resultStack.pop()
    }
}