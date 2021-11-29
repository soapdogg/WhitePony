package compiler.printer.impl

import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.printer.impl.internal.ICodeGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class TranslatedExpressionStatementPrinterTest {
    private val codeGenerator = Mockito.mock(ICodeGenerator::class.java)
    private val translatedExpressionStatementPrinter = TranslatedExpressionStatementPrinter(codeGenerator)

    @Test
    fun printNodeTest() {
        val node = Mockito.mock(TranslatedExpressionStatementNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.START
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        val appendSemicolon = false

        val expression = Mockito.mock(TranslatedExpressionNode::class.java)
        Mockito.`when`(node.expression).thenReturn(expression)

        val code = listOf("code")
        Mockito.`when`(expression.code).thenReturn(code)

        val result = "result"
        Mockito.`when`(codeGenerator.generateCode(code)).thenReturn(result)

        translatedExpressionStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, appendSemicolon)
        val actual = resultStack.pop()
        Assertions.assertEquals(result, actual)
    }
}