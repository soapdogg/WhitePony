package compiler.frontend.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedBasicBlockNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.frontend.printer.impl.internal.IStatementPrinterStackPusher
import compiler.frontend.printer.impl.internal.ITabsGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ParsedBasicBlockStatementPrinterTest {
    private val statementPrinterStackPusher = Mockito.mock(IStatementPrinterStackPusher::class.java)
    private val tabsGenerator = Mockito.mock(ITabsGenerator::class.java)

    private val parsedBasicBlockStatementPrinter = ParsedBasicBlockStatementPrinter(
        statementPrinterStackPusher,
        tabsGenerator
    )

    @Test
    fun startLocationTest() {
        val node = Mockito.mock(ParsedBasicBlockNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.START
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        val appendSemicolon = true

        val statement1 = Mockito.mock(IParsedStatementNode::class.java)
        val statement2 = Mockito.mock(IParsedStatementNode::class.java)
        val statements = listOf(statement1, statement2)
        Mockito.`when`(node.statements).thenReturn(statements)

        parsedBasicBlockStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, appendSemicolon)
        Mockito.verify(statementPrinterStackPusher).push(node, numberOfTabs, StatementPrinterLocation.END, stack)
        Mockito.verify(statementPrinterStackPusher).push(statement1, numberOfTabs + 1, StatementPrinterLocation.START, stack)
        Mockito.verify(statementPrinterStackPusher).push(statement2, numberOfTabs + 1, StatementPrinterLocation.START, stack)
    }

    @Test
    fun endLocationTest() {
        val node = Mockito.mock(ParsedBasicBlockNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.END
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        val appendSemicolon = true

        val tabs = "tabs"
        Mockito.`when`(tabsGenerator.generateTabs(numberOfTabs + 1)).thenReturn(tabs)

        val closingTabs = "closingTabs"
        Mockito.`when`(tabsGenerator.generateTabs(numberOfTabs)).thenReturn(closingTabs)

        val statement1 = Mockito.mock(IParsedStatementNode::class.java)
        val statement2 = Mockito.mock(IParsedStatementNode::class.java)
        val statements = listOf(statement1, statement2)
        Mockito.`when`(node.statements).thenReturn(statements)

        val statement2Code = "statement2Code"
        resultStack.push(statement2Code)
        val statement1Code = "statement1Code"
        resultStack.push(statement1Code)

        val expected = PrinterConstants.LEFT_BRACE + PrinterConstants.NEW_LINE + tabs + statement1Code + PrinterConstants.NEW_LINE + tabs + statement2Code + PrinterConstants.NEW_LINE + closingTabs + PrinterConstants.RIGHT_BRACE
        parsedBasicBlockStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, appendSemicolon)
        val top = resultStack.pop()
        Assertions.assertEquals(expected, top)
    }
}