package compiler.frontend.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedForNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.frontend.printer.impl.internal.IExpressionPrinter
import compiler.frontend.printer.impl.internal.IStatementPrinterStackPusher
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ParsedForStatementPrinterTest {
    private val statementPrinterStackPusher = Mockito.mock(IStatementPrinterStackPusher::class.java)
    private val expressionPrinter = Mockito.mock(IExpressionPrinter::class.java)

    private val parsedForStatementPrinter = ParsedForStatementPrinter(
        statementPrinterStackPusher,
        expressionPrinter
    )

    @Test
    fun startLocationTest() {
        val node = Mockito.mock(ParsedForNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.START
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()

        val body = Mockito.mock(IParsedStatementNode::class.java)
        Mockito.`when`(node.body).thenReturn(body)

        parsedForStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, false)

        Mockito.verify(statementPrinterStackPusher).push(node, numberOfTabs, StatementPrinterLocation.END, stack)
        Mockito.verify(statementPrinterStackPusher).push(body, numberOfTabs, StatementPrinterLocation.START, stack)
    }

    @Test
    fun endDoWhileLocationTest() {
        val node = Mockito.mock(ParsedForNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.END
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()

        val bodyString = "bodyString"
        resultStack.push(bodyString)

        val initExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.initExpression).thenReturn(initExpression)

        val initExpressionString = "initExpressionString"
        Mockito.`when`(expressionPrinter.printNode(initExpression)).thenReturn(initExpressionString)

        val testExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.testExpression).thenReturn(testExpression)

        val testExpressionString = "testExpressionString"
        Mockito.`when`(expressionPrinter.printNode(testExpression)).thenReturn(testExpressionString)

        val incrementExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.incrementExpression).thenReturn(incrementExpression)

        val incrementExpressionString = "incrementExpressionString"
        Mockito.`when`(expressionPrinter.printNode(incrementExpression)).thenReturn(incrementExpressionString)

        val expected = PrinterConstants.FOR +
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
        parsedForStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, false)
        val actual = resultStack.pop()
        Assertions.assertEquals(expected, actual)
    }
}