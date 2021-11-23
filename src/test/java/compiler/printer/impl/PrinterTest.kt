package compiler.printer.impl

import compiler.core.IParsedDeclarationStatementNode
import compiler.core.ParsedProgramRootNode
import compiler.printer.impl.internal.IDeclarationStatementPrinter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class PrinterTest {
    private val declarationStatementPrinter = Mockito.mock(IDeclarationStatementPrinter::class.java)

    private val printer = Printer(declarationStatementPrinter)

    @Test
    fun printParsedNodeTest() {
        val parsedRootNode = Mockito.mock(ParsedProgramRootNode::class.java)

        val declarationStatement1 = Mockito.mock(IParsedDeclarationStatementNode::class.java)
        val declarationStatement2 = Mockito.mock(IParsedDeclarationStatementNode::class.java)
        Mockito.`when`(parsedRootNode.declarationStatements).thenReturn(listOf(declarationStatement1, declarationStatement2))

        val s1 = "s1"
        Mockito.`when`(declarationStatementPrinter.printNode(declarationStatement1)).thenReturn(s1)

        val s2 = "s2"
        Mockito.`when`(declarationStatementPrinter.printNode(declarationStatement2)).thenReturn(s2)

        val expected = s1 + "\n" + s2
        val actual = printer.printNode(parsedRootNode)

        Assertions.assertEquals(expected, actual)
    }
}