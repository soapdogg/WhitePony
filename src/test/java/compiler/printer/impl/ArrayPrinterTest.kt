package compiler.printer.impl

import compiler.core.IParsedExpressionNode
import compiler.core.ParsedArrayNode
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IExpressionPrinter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ArrayPrinterTest {
    private val expressionPrinter = Mockito.mock(IExpressionPrinter::class.java)

    private val arrayPrinter = ArrayPrinter(expressionPrinter)

    @Test
    fun noIndexTest() {
        val node = Mockito.mock(ParsedArrayNode::class.java)
        val expected = PrinterConstants.LEFT_BRACKET + PrinterConstants.RIGHT_BRACKET
        val actual = arrayPrinter.printParsedNode(node)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun indexTest() {
        val node = Mockito.mock(ParsedArrayNode::class.java)

        val indexNode = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.indexExpressionNode).thenReturn(indexNode)

        val indexString = "indexString"
        Mockito.`when`(expressionPrinter.printParsedNode(indexNode)).thenReturn(indexString)

        val expected = PrinterConstants.LEFT_BRACKET + indexString + PrinterConstants.RIGHT_BRACKET
        val actual = arrayPrinter.printParsedNode(node)

        Assertions.assertEquals(expected, actual)
    }
}