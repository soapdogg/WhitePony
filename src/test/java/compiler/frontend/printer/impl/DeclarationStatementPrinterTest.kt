package compiler.frontend.printer.impl

import compiler.core.nodes.parsed.ParsedFunctionDeclarationNode
import compiler.core.nodes.VariableDeclarationListNode
import compiler.frontend.printer.impl.internal.IFunctionDeclarationPrinter
import compiler.frontend.printer.impl.internal.IVariableDeclarationListPrinter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class DeclarationStatementPrinterTest {

    private val functionDeclarationPrinter = Mockito.mock(IFunctionDeclarationPrinter::class.java)
    private val variableDeclarationListPrinter = Mockito.mock(IVariableDeclarationListPrinter::class.java)

    private val declarationStatementPrinter = DeclarationStatementPrinter(
        functionDeclarationPrinter,
        variableDeclarationListPrinter
    )

    @Test
    fun printParsedNodeVariableListTest() {
        val parsedDeclarationStatementNode = Mockito.mock(VariableDeclarationListNode::class.java)

        val s1 = "s1"
        Mockito.`when`(variableDeclarationListPrinter.printNode(
            parsedDeclarationStatementNode,
            true
        )).thenReturn(s1)

        val actual = declarationStatementPrinter.printNode(parsedDeclarationStatementNode, false)

        Assertions.assertEquals(s1, actual)
    }

    @Test
    fun printParsedNodeFunctionTest() {
        val parsedDeclarationStatementNode = Mockito.mock(ParsedFunctionDeclarationNode::class.java)

        val s1 = "s1"
        Mockito.`when`(functionDeclarationPrinter.printNode(parsedDeclarationStatementNode, false)).thenReturn(s1)

        val actual = declarationStatementPrinter.printNode(parsedDeclarationStatementNode, false)

        Assertions.assertEquals(s1, actual)
    }
}