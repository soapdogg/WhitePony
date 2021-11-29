package compiler.printer.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedExpressionStatementNode
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IExpressionPrinter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ExpressionStatementPrinterTest {
    private val expressionPrinter = Mockito.mock(IExpressionPrinter::class.java)
    private val expressionStatementPrinter = ExpressionStatementPrinter(expressionPrinter)

    @Test
    fun printParsedNodeTest() {
        val node = Mockito.mock(ParsedExpressionStatementNode::class.java)

        val expression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.expressionNode).thenReturn(expression)

        val expressionString = "expressionString"
        Mockito.`when`(expressionPrinter.printNode(expression)).thenReturn(expressionString)

        val expected = expressionString + PrinterConstants.SEMICOLON
        val actual = expressionStatementPrinter.printNode(node)

        Assertions.assertEquals(expected, actual)
    }
}