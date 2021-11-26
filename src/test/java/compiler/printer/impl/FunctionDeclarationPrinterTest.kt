package compiler.printer.impl

import compiler.core.nodes.parsed.ParsedBasicBlockNode
import compiler.core.nodes.parsed.ParsedFunctionDeclarationNode
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IStatementPrinter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class FunctionDeclarationPrinterTest {
    private val statementPrinter = Mockito.mock(IStatementPrinter::class.java)
    private val functionDeclarationPrinter = FunctionDeclarationPrinter(statementPrinter)

    @Test
    fun printParsedNodeTest() {
        val node = Mockito.mock(ParsedFunctionDeclarationNode::class.java)

        val type = "type"
        Mockito.`when`(node.type).thenReturn(type)

        val functionName = "functionName"
        Mockito.`when`(node.functionName).thenReturn(functionName)

        val basicBlockNode = Mockito.mock(ParsedBasicBlockNode::class.java)
        Mockito.`when`(node.basicBlockNode).thenReturn(basicBlockNode)

        val basicBlockString = "basicBlockString"
        Mockito.`when`(statementPrinter.printNode(basicBlockNode, PrinterConstants.INITIAL_NUMBER_OF_TABS)).thenReturn(basicBlockString)

        val expected = type +
                PrinterConstants.SPACE +
                functionName +
                PrinterConstants.LEFT_PARENTHESES +
                PrinterConstants.RIGHT_PARENTHESES +
                PrinterConstants.SPACE +
                basicBlockString

        val actual = functionDeclarationPrinter.printNode(node)

        Assertions.assertEquals(expected, actual)
    }
}