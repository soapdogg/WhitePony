package compiler.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedDoWhileNode
import compiler.core.nodes.parsed.ParsedWhileNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.printer.impl.internal.IExpressionPrinter
import compiler.printer.impl.internal.IStatementPrinterStackPusher
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ParsedWhileStatementPrinterTest {
    private val statementPrinterStackPusher = Mockito.mock(IStatementPrinterStackPusher::class.java)
    private val expressionPrinter = Mockito.mock(IExpressionPrinter::class.java)

    private val parsedWhileStatementPrinter = ParsedWhileStatementPrinter(
        statementPrinterStackPusher,
        expressionPrinter
    )

    @Test
    fun startLocationTest() {
        val node = Mockito.mock(ParsedWhileNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.START
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()

        val body = Mockito.mock(IParsedStatementNode::class.java)
        Mockito.`when`(node.body).thenReturn(body)

        parsedWhileStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, false)

        Mockito.verify(statementPrinterStackPusher).push(node, numberOfTabs, StatementPrinterLocation.END_WHILE, stack)
        Mockito.verify(statementPrinterStackPusher).push(body, numberOfTabs, StatementPrinterLocation.START, stack)
    }

    @Test
    fun endDoWhileLocationTest() {
        val node = Mockito.mock(ParsedWhileNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.END_WHILE
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()

        val bodyString = "bodyString"
        resultStack.push(bodyString)

        val expression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.expression).thenReturn(expression)

        val expressionString = "expressionString"
        Mockito.`when`(expressionPrinter.printNode(expression)).thenReturn(expressionString)

        val expected = PrinterConstants.WHILE +
                PrinterConstants.SPACE +
                expressionString +
                PrinterConstants.SPACE +
                bodyString
        parsedWhileStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, false)
        val actual = resultStack.pop()
        Assertions.assertEquals(expected, actual)
    }
}