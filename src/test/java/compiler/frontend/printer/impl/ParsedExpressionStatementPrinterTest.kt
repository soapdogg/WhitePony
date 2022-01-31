package compiler.frontend.printer.impl

import compiler.core.nodes.parsed.ParsedExpressionStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.frontend.printer.impl.internal.IExpressionStatementPrinter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ParsedExpressionStatementPrinterTest {
    private val expressionStatementPrinter = Mockito.mock(IExpressionStatementPrinter::class.java)
    private val parsedExpressionStatementPrinter = ParsedExpressionStatementPrinter(expressionStatementPrinter)

    @Test
    fun printNodeTest() {
        val node = Mockito.mock(ParsedExpressionStatementNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.START
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        val appendSemicolon = false

        val result = "result"
        Mockito.`when`(expressionStatementPrinter.printNode(node)).thenReturn(result)

        parsedExpressionStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, appendSemicolon)
        val top = resultStack.pop()
        Assertions.assertEquals(result, top)
    }
}