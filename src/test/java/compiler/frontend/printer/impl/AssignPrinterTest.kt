package compiler.frontend.printer.impl

import compiler.core.nodes.AssignNode
import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.constants.PrinterConstants
import compiler.frontend.printer.impl.internal.IExpressionPrinter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class AssignPrinterTest {
    private val expressionPrinter = Mockito.mock(IExpressionPrinter::class.java)
    private val assignPrinter = AssignPrinter(expressionPrinter)

    @Test
    fun printParsedNodeTest() {
        val assignNode = Mockito.mock(AssignNode::class.java)

        val expressionNode = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(assignNode.expressionNode).thenReturn(expressionNode)

        val expressionString = "expressionString"
        Mockito.`when`(expressionPrinter.printNode(expressionNode)).thenReturn(expressionString)

        val expected = PrinterConstants.SPACE + PrinterConstants.EQUALS + PrinterConstants.SPACE + expressionString
        val actual = assignPrinter.printNode(assignNode)

        Assertions.assertEquals(expected, actual)
    }
}