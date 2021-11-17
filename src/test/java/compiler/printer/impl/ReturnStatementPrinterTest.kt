package compiler.printer.impl

import compiler.core.ParsedExpressionStatementNode
import compiler.core.ParsedReturnNode
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IExpressionStatementPrinter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ReturnStatementPrinterTest {
    private val expressionStatementPrinter = Mockito.mock(IExpressionStatementPrinter::class.java)
    private val returnStatementPrinter = ReturnStatementPrinter(expressionStatementPrinter)

    @Test
    fun printParsedNodeTest() {
        val node = Mockito.mock(ParsedReturnNode::class.java)

        val expressionStatement = Mockito.mock(ParsedExpressionStatementNode::class.java)
        Mockito.`when`(node.expressionStatement).thenReturn(expressionStatement)

        val expressionStatementString = "expressionStatement"
        Mockito.`when`(expressionStatementPrinter.printParsedNode(expressionStatement)).thenReturn(expressionStatementString)

        val expected = PrinterConstants.RETURN + PrinterConstants.SPACE + expressionStatementString
        val actual = returnStatementPrinter.printParsedNode(node)

        Assertions.assertEquals(expected, actual)
    }
}