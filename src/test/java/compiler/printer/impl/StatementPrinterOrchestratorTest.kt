package compiler.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.IStatementNode
import compiler.core.nodes.parsed.ParsedBasicBlockNode
import compiler.core.nodes.translated.TranslatedBasicBlockNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.printer.impl.internal.IStackGenerator
import compiler.printer.impl.internal.IStatementPrinter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class StatementPrinterOrchestratorTest {
    private val stackGenerator = Mockito.mock(IStackGenerator::class.java)

    @Test
    fun topIsTranslatedTest() {
        val printer = Mockito.mock(IStatementPrinter::class.java)
        val printerMap = mapOf<Class<out IStatementNode>, IStatementPrinter>(
            TranslatedBasicBlockNode::class.java to printer
        )

        val statementPrinterOrchestrator = StatementPrinterOrchestrator(stackGenerator, printerMap)

        val node = Mockito.mock(TranslatedBasicBlockNode::class.java)
        val numberOfTabs = 0
        val appendSemicolon = true

        val stack = Stack<StatementPrinterStackItem>()
        Mockito.`when`(stackGenerator.generateNewStack(StatementPrinterStackItem::class.java)).thenReturn(stack)

        val resultStack = Stack<String>()
        Mockito.`when`(stackGenerator.generateNewStack(String::class.java)).thenReturn(resultStack)

        val result = "result"
        Mockito.`when`(
            printer.printNode(
                node,
                numberOfTabs,
                StatementPrinterLocation.START,
                stack,
                resultStack,
                appendSemicolon
            )
        ).then{
            resultStack.push(result)
        }

        val expected = PrinterConstants.LEFT_BRACE + PrinterConstants.TABBED_NEW_LINE + result + PrinterConstants.NEW_LINE + PrinterConstants.RIGHT_BRACE
        val actual = statementPrinterOrchestrator.printNode(node, numberOfTabs, appendSemicolon)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun topIsNotTranslatedTest() {
        val printer = Mockito.mock(IStatementPrinter::class.java)
        val printerMap = mapOf<Class<out IStatementNode>, IStatementPrinter>(
            ParsedBasicBlockNode::class.java to printer
        )

        val statementPrinterOrchestrator = StatementPrinterOrchestrator(stackGenerator, printerMap)

        val node = Mockito.mock(ParsedBasicBlockNode::class.java)
        val numberOfTabs = 0
        val appendSemicolon = true

        val stack = Stack<StatementPrinterStackItem>()
        Mockito.`when`(stackGenerator.generateNewStack(StatementPrinterStackItem::class.java)).thenReturn(stack)

        val resultStack = Stack<String>()
        Mockito.`when`(stackGenerator.generateNewStack(String::class.java)).thenReturn(resultStack)

        val result = "result"
        Mockito.`when`(
            printer.printNode(
                node,
                numberOfTabs,
                StatementPrinterLocation.START,
                stack,
                resultStack,
                appendSemicolon
            )
        ).then{
            resultStack.push(result)
        }

        val actual = statementPrinterOrchestrator.printNode(node, numberOfTabs, appendSemicolon)
        Assertions.assertEquals(result, actual)
    }
}