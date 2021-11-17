package compiler.printer.impl

import compiler.core.ParsedArrayNode
import compiler.core.ParsedAssignNode
import compiler.core.ParsedVariableDeclarationNode
import compiler.printer.impl.internal.IArrayPrinter
import compiler.printer.impl.internal.IAssignPrinter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class VariableDeclarationPrinterTest {
    private val arrayPrinter = Mockito.mock(IArrayPrinter::class.java)
    private val assignPrinter = Mockito.mock(IAssignPrinter::class.java)

    private val variableDeclarationPrinter = VariableDeclarationPrinter(
        arrayPrinter,
        assignPrinter
    )

    @Test
    fun bothPresentTest() {
        val node = Mockito.mock(ParsedVariableDeclarationNode::class.java)

        val arrayNode = Mockito.mock(ParsedArrayNode::class.java)
        Mockito.`when`(node.arrayNode).thenReturn(arrayNode)

        val arrayString = "arrayString"
        Mockito.`when`(arrayPrinter.printParsedNode(arrayNode)).thenReturn(arrayString)

        val assignNode = Mockito.mock(ParsedAssignNode::class.java)
        Mockito.`when`(node.assignNode).thenReturn(assignNode)

        val assignString = "assignString"
        Mockito.`when`(assignPrinter.printParsedNode(assignNode)).thenReturn(assignString)

        val id = "id"
        Mockito.`when`(node.id).thenReturn(id)

        val expected = id + arrayString + assignString
        val actual = variableDeclarationPrinter.printParsedNode(node)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun neitherPresentTest() {
        val node = Mockito.mock(ParsedVariableDeclarationNode::class.java)

        val id = "id"
        Mockito.`when`(node.id).thenReturn(id)

        val actual = variableDeclarationPrinter.printParsedNode(node)

        Assertions.assertEquals(id, actual)
    }
}