package compiler.frontend.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedDoWhileNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.frontend.printer.impl.internal.IExpressionPrinter
import compiler.frontend.printer.impl.internal.IStatementPrinterStackPusher
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ParsedDoWhileStatementPrinterTest {
    private val statementPrinterStackPusher = Mockito.mock(IStatementPrinterStackPusher::class.java)
    private val expressionPrinter = Mockito.mock(IExpressionPrinter::class.java)

    private val parsedDoWhileStatementPrinter = ParsedDoWhileStatementPrinter(
        statementPrinterStackPusher,
        expressionPrinter
    )

    @Test
    fun startLocationTest() {
        val node = Mockito.mock(ParsedDoWhileNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.START
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()

        val body = Mockito.mock(IParsedStatementNode::class.java)
        Mockito.`when`(node.body).thenReturn(body)

        parsedDoWhileStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, false)

        Mockito.verify(statementPrinterStackPusher).push(node, numberOfTabs, StatementPrinterLocation.END, stack)
        Mockito.verify(statementPrinterStackPusher).push(body, numberOfTabs, StatementPrinterLocation.START, stack)
    }

    @Test
    fun endDoWhileLocationTest() {
        val node = Mockito.mock(ParsedDoWhileNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.END
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()

        val bodyString = "bodyString"
        resultStack.push(bodyString)

        val expression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.expression).thenReturn(expression)

        val expressionString = "expressionString"
        Mockito.`when`(expressionPrinter.printNode(expression)).thenReturn(expressionString)

        val expected = PrinterConstants.DO +
                PrinterConstants.SPACE +
                bodyString +
                PrinterConstants.SPACE +
                PrinterConstants.WHILE +
                PrinterConstants.SPACE +
                expressionString +
                PrinterConstants.SEMICOLON
        parsedDoWhileStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, false)
        val actual = resultStack.pop()
        Assertions.assertEquals(expected, actual)
    }
}