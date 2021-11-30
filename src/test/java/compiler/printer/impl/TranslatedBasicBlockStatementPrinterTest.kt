package compiler.printer.impl

import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.nodes.translated.TranslatedBasicBlockNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.printer.impl.internal.ICodeGenerator
import compiler.printer.impl.internal.IStatementPrinterStackPusher
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class TranslatedBasicBlockStatementPrinterTest {
    private val statementPrinterStackPusher = Mockito.mock(IStatementPrinterStackPusher::class.java)
    private val codeGenerator = Mockito.mock(ICodeGenerator::class.java)

    private val translatedBasicBlockStatementPrinter = TranslatedBasicBlockStatementPrinter(
        statementPrinterStackPusher,
        codeGenerator
    )

    @Test
    fun startLocationTest() {
        val node = Mockito.mock(TranslatedBasicBlockNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.START
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        val appendSemicolon = true

        val statement1 = Mockito.mock(ITranslatedStatementNode::class.java)
        val statement2 = Mockito.mock(ITranslatedStatementNode::class.java)
        val statements = listOf(statement1, statement2)
        Mockito.`when`(node.statements).thenReturn(statements)

        translatedBasicBlockStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, appendSemicolon)
        Mockito.verify(statementPrinterStackPusher).push(node, numberOfTabs, StatementPrinterLocation.END, stack)
        Mockito.verify(statementPrinterStackPusher).push(statement1, numberOfTabs, StatementPrinterLocation.START, stack)
        Mockito.verify(statementPrinterStackPusher).push(statement2, numberOfTabs, StatementPrinterLocation.START, stack)
    }

    @Test
    fun endLocationTest() {
        val node = Mockito.mock(TranslatedBasicBlockNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.END
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        val appendSemicolon = true

        val statement1 = Mockito.mock(ITranslatedStatementNode::class.java)
        val statement2 = Mockito.mock(ITranslatedStatementNode::class.java)
        val statements = listOf(statement1, statement2)
        Mockito.`when`(node.statements).thenReturn(statements)

        val statement2Code = "statement2Code"
        resultStack.push(statement2Code)
        val statement1Code = "statement1Code"
        resultStack.push(statement1Code)

        val result = "result"
        Mockito.`when`(codeGenerator.generateCode(listOf(statement1Code, statement2Code))).thenReturn(result)

        translatedBasicBlockStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, appendSemicolon)
        val top = resultStack.pop()
        Assertions.assertEquals(result, top)
    }
}