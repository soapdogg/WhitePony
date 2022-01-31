package compiler.frontend.printer.impl

import compiler.core.nodes.VariableDeclarationListNode
import compiler.core.nodes.VariableDeclarationNode
import compiler.core.constants.PrinterConstants
import compiler.frontend.printer.impl.internal.IVariableDeclarationPrinter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class VariableDeclarationListPrinterTest {
    private val variableDeclarationPrinter = Mockito.mock(IVariableDeclarationPrinter::class.java)
    private val variableDeclarationListPrinter = VariableDeclarationListPrinter(variableDeclarationPrinter)

    @Test
    fun printParsedNodeTest() {
        val node = Mockito.mock(VariableDeclarationListNode::class.java)

        val variableDeclaration1 = Mockito.mock(VariableDeclarationNode::class.java)
        val variableDeclaration2 = Mockito.mock(VariableDeclarationNode::class.java)
        Mockito.`when`(node.variableDeclarations).thenReturn(listOf(variableDeclaration1, variableDeclaration2))

        val s1 = "s1"
        Mockito.`when`(variableDeclarationPrinter.printNode(variableDeclaration1)).thenReturn(s1)

        val s2 = "s2"
        Mockito.`when`(variableDeclarationPrinter.printNode(variableDeclaration2)).thenReturn(s2)

        val type = "type"
        Mockito.`when`(node.type).thenReturn(type)

        val expected = type + PrinterConstants.SPACE + s1 + PrinterConstants.COMMA + PrinterConstants.SPACE + s2 + PrinterConstants.SEMICOLON
        val actual = variableDeclarationListPrinter.printNode(
            node,
            true
        )

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun printParsedNodeWithoutSemicolonTest() {
        val node = Mockito.mock(VariableDeclarationListNode::class.java)

        val variableDeclaration1 = Mockito.mock(VariableDeclarationNode::class.java)
        val variableDeclaration2 = Mockito.mock(VariableDeclarationNode::class.java)
        Mockito.`when`(node.variableDeclarations).thenReturn(listOf(variableDeclaration1, variableDeclaration2))

        val s1 = "s1"
        Mockito.`when`(variableDeclarationPrinter.printNode(variableDeclaration1)).thenReturn(s1)

        val s2 = "s2"
        Mockito.`when`(variableDeclarationPrinter.printNode(variableDeclaration2)).thenReturn(s2)

        val type = "type"
        Mockito.`when`(node.type).thenReturn(type)

        val expected = type + PrinterConstants.SPACE + s1 + PrinterConstants.COMMA + PrinterConstants.SPACE + s2
        val actual = variableDeclarationListPrinter.printNode(
            node,
            false
        )

        Assertions.assertEquals(expected, actual)
    }
}