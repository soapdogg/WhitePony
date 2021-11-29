package compiler.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedIfNode
import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.nodes.translated.TranslatedIfNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.printer.impl.internal.IExpressionPrinter
import compiler.printer.impl.internal.IStatementPrinterStackPusher
import compiler.printer.impl.internal.ITabsGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ParsedIfStatementPrinterTest {
    private val statementPrinterStackPusher = Mockito.mock(IStatementPrinterStackPusher::class.java)
    private val expressionPrinter = Mockito.mock(IExpressionPrinter::class.java)
    private val tabsGenerator = Mockito.mock(ITabsGenerator::class.java)

    private val parsedIfStatementPrinter = ParsedIfStatementPrinter(
        statementPrinterStackPusher,
        expressionPrinter,
        tabsGenerator
    )

    @Test
    fun startLocationElseNotPresentTest() {
        val node = Mockito.mock(ParsedIfNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.START
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        val appendSemicolon = true

        val ifBody = Mockito.mock(IParsedStatementNode::class.java)
        Mockito.`when`(node.ifBody).thenReturn(ifBody)

        parsedIfStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, appendSemicolon)
        Mockito.verify(statementPrinterStackPusher).push(node, numberOfTabs, StatementPrinterLocation.END_IF, stack)
        Mockito.verify(statementPrinterStackPusher).push(ifBody, numberOfTabs, StatementPrinterLocation.START, stack)
    }

    @Test
    fun startLocationElsePresentTest() {
        val node = Mockito.mock(ParsedIfNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.START
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        val appendSemicolon = true

        val ifBody = Mockito.mock(IParsedStatementNode::class.java)
        Mockito.`when`(node.ifBody).thenReturn(ifBody)

        val elseBody = Mockito.mock(IParsedStatementNode::class.java)
        Mockito.`when`(node.elseBody).thenReturn(elseBody)

        parsedIfStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, appendSemicolon)
        Mockito.verify(statementPrinterStackPusher).push(node, numberOfTabs, StatementPrinterLocation.END_IF, stack)
        Mockito.verify(statementPrinterStackPusher).push(ifBody, numberOfTabs, StatementPrinterLocation.START, stack)
        Mockito.verify(statementPrinterStackPusher).push(elseBody, numberOfTabs, StatementPrinterLocation.START, stack)
    }

    @Test
    fun endLocationElseNotPresentTest() {
        val node = Mockito.mock(ParsedIfNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.END_IF
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        val appendSemicolon = true

        val ifBodyCode = "ifBodyCode"
        resultStack.push(ifBodyCode)

        val booleanExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.booleanExpression).thenReturn(booleanExpression)

        val booleanExpressionCode = "booleanExpressionCode"
        Mockito.`when`(expressionPrinter.printNode(booleanExpression)).thenReturn(booleanExpressionCode)

        val ifString = PrinterConstants.IF +
                booleanExpressionCode +
                PrinterConstants.SPACE +
                ifBodyCode

        parsedIfStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, appendSemicolon)
        val top = resultStack.pop()
        Assertions.assertEquals(ifString, top)
    }

    @Test
    fun endLocationElsePresentTest() {
        val node = Mockito.mock(ParsedIfNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.END_IF
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        val appendSemicolon = true

        val elseBodyCode = "elseBodyCode"
        resultStack.push(elseBodyCode)

        val ifBodyCode = "ifBodyCode"
        resultStack.push(ifBodyCode)

        val booleanExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.booleanExpression).thenReturn(booleanExpression)

        val booleanExpressionCode = "booleanExpressionCode"
        Mockito.`when`(expressionPrinter.printNode(booleanExpression)).thenReturn(booleanExpressionCode)

        val ifString = PrinterConstants.IF +
                booleanExpressionCode +
                PrinterConstants.SPACE +
                ifBodyCode

        val elseBody = Mockito.mock(IParsedStatementNode::class.java)
        Mockito.`when`(node.elseBody).thenReturn(elseBody)

        val tabs = "tabs"
        Mockito.`when`(tabsGenerator.generateTabs(numberOfTabs)).thenReturn(tabs)

        val expected = ifString + PrinterConstants.NEW_LINE + tabs + PrinterConstants.ELSE + PrinterConstants.SPACE + elseBodyCode

        parsedIfStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, appendSemicolon)
        val top = resultStack.pop()
        Assertions.assertEquals(expected, top)
    }
}