package compiler.printer.impl

import compiler.core.IParsedExpressionNode
import compiler.core.ParsedAssignNode
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IExpressionPrinter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class AssignPrinterTest {
    private val expressionPrinter = Mockito.mock(IExpressionPrinter::class.java)
    private val assignPrinter = AssignPrinter(expressionPrinter)

    @Test
    fun printParsedNodeTest() {
        val assignNode = Mockito.mock(ParsedAssignNode::class.java)

        val expressionNode = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(assignNode.expressionNode).thenReturn(expressionNode)

        val expressionString = "expressionString"
        Mockito.`when`(expressionPrinter.printParsedNode(expressionNode)).thenReturn(expressionString)

        val expected = PrinterConstants.SPACE + PrinterConstants.EQUALS + PrinterConstants.SPACE + expressionString
        val actual = assignPrinter.printParsedNode(assignNode)

        Assertions.assertEquals(expected, actual)
    }
}