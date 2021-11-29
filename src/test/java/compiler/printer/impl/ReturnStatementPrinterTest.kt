package compiler.printer.impl

import compiler.core.nodes.parsed.ParsedExpressionStatementNode
import compiler.core.nodes.parsed.ParsedReturnNode
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
        Mockito.`when`(expressionStatementPrinter.printNode(expressionStatement)).thenReturn(expressionStatementString)

        val expected = PrinterConstants.RETURN + PrinterConstants.SPACE + expressionStatementString
        val actual = returnStatementPrinter.printNode(node)

        Assertions.assertEquals(expected, actual)
    }
}