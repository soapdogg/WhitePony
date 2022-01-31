package compiler.frontend.printer.impl

import compiler.core.nodes.parsed.ParsedReturnNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.frontend.printer.impl.internal.IReturnStatementPrinter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ParsedReturnStatementPrinterTest {
    private val returnStatementPrinter = Mockito.mock(IReturnStatementPrinter::class.java)
    private val parsedReturnStatementPrinter = ParsedReturnStatementPrinter(returnStatementPrinter)

    @Test
    fun printNodeTest() {
        val node = Mockito.mock(ParsedReturnNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.START
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        val appendSemicolon = false

        val result = "result"
        Mockito.`when`(returnStatementPrinter.printNode(node)).thenReturn(result)

        parsedReturnStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, appendSemicolon)
        val top = resultStack.pop()
        Assertions.assertEquals(result, top)
    }
}