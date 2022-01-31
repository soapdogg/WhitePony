package compiler.frontend.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionStatementNode
import compiler.core.nodes.translated.TranslatedReturnNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.frontend.printer.impl.internal.ICodeGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class TranslatedReturnStatementPrinterTest {
    private val codeGenerator = Mockito.mock(ICodeGenerator::class.java)
    private val translatedReturnStatementPrinter = TranslatedReturnStatementPrinter(codeGenerator)

    @Test
    fun printNodeTest() {
        val node = Mockito.mock(TranslatedReturnNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.START
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        val appendSemicolon = false

        val expressionStatement = Mockito.mock(TranslatedExpressionStatementNode::class.java)
        Mockito.`when`(node.expressionStatement).thenReturn(expressionStatement)

        val expression = Mockito.mock(TranslatedExpressionNode::class.java)
        Mockito.`when`(expressionStatement.expression).thenReturn(expression)

        val address = "address"
        Mockito.`when`(expression.address).thenReturn(address)

        val returnCode = PrinterConstants.RETURN + PrinterConstants.SPACE + address

        val code = listOf("code")
        Mockito.`when`(expression.code).thenReturn(code)

        val result = "result"
        Mockito.`when`(codeGenerator.generateCode(code + listOf(returnCode))).thenReturn(result)

        translatedReturnStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, appendSemicolon)
        val top = resultStack.pop()
        Assertions.assertEquals(result + PrinterConstants.SEMICOLON, top)
    }
}