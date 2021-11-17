package compiler.printer.impl

import compiler.core.ParsedVariableDeclarationListNode
import compiler.core.ParsedVariableDeclarationNode
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IVariableDeclarationPrinter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class VariableDeclarationListPrinterTest {
    private val variableDeclarationPrinter = Mockito.mock(IVariableDeclarationPrinter::class.java)
    private val variableDeclarationListPrinter = VariableDeclarationListPrinter(variableDeclarationPrinter)

    @Test
    fun printParsedNodeTest() {
        val node = Mockito.mock(ParsedVariableDeclarationListNode::class.java)

        val variableDeclaration1 = Mockito.mock(ParsedVariableDeclarationNode::class.java)
        val variableDeclaration2 = Mockito.mock(ParsedVariableDeclarationNode::class.java)
        Mockito.`when`(node.variableDeclarations).thenReturn(listOf(variableDeclaration1, variableDeclaration2))

        val s1 = "s1"
        Mockito.`when`(variableDeclarationPrinter.printParsedNode(variableDeclaration1)).thenReturn(s1)

        val s2 = "s2"
        Mockito.`when`(variableDeclarationPrinter.printParsedNode(variableDeclaration2)).thenReturn(s2)

        val type = "type"
        Mockito.`when`(node.type).thenReturn(type)

        val expected = type + PrinterConstants.SPACE + s1 + PrinterConstants.COMMA + PrinterConstants.SPACE + s2 + PrinterConstants.SEMICOLON
        val actual = variableDeclarationListPrinter.printParsedNode(node)

        Assertions.assertEquals(expected, actual)
    }
}