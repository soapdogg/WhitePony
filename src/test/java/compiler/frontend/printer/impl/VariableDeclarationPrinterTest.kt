package compiler.frontend.printer.impl

import compiler.core.nodes.ArrayNode
import compiler.core.nodes.AssignNode
import compiler.core.nodes.VariableDeclarationNode
import compiler.frontend.printer.impl.internal.IArrayPrinter
import compiler.frontend.printer.impl.internal.IAssignPrinter
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
        val node = Mockito.mock(VariableDeclarationNode::class.java)

        val arrayNode = Mockito.mock(ArrayNode::class.java)
        Mockito.`when`(node.arrayNode).thenReturn(arrayNode)

        val arrayString = "arrayString"
        Mockito.`when`(arrayPrinter.printNode(arrayNode)).thenReturn(arrayString)

        val assignNode = Mockito.mock(AssignNode::class.java)
        Mockito.`when`(node.assignNode).thenReturn(assignNode)

        val assignString = "assignString"
        Mockito.`when`(assignPrinter.printNode(assignNode)).thenReturn(assignString)

        val id = "id"
        Mockito.`when`(node.id).thenReturn(id)

        val expected = id + arrayString + assignString
        val actual = variableDeclarationPrinter.printNode(node)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun neitherPresentTest() {
        val node = Mockito.mock(VariableDeclarationNode::class.java)

        val id = "id"
        Mockito.`when`(node.id).thenReturn(id)

        val actual = variableDeclarationPrinter.printNode(node)

        Assertions.assertEquals(id, actual)
    }
}