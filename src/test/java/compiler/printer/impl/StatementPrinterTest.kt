package compiler.printer.impl

import compiler.core.IParsedStatementNode
import compiler.core.StatementPrinterStackItem
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IStatementPrinterResultGenerator
import compiler.printer.impl.internal.IStatementPrinterStackItemGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class StatementPrinterTest {
    private val statementPrinterStackItemGenerator = Mockito.mock(IStatementPrinterStackItemGenerator::class.java)
    private val statementPrinterResultGenerator = Mockito.mock(IStatementPrinterResultGenerator::class.java)

    private val statementPrinter = StatementPrinter(
        statementPrinterStackItemGenerator,
        statementPrinterResultGenerator
    )

    @Test
    fun printParsedNodeTest() {
        val node = Mockito.mock(IParsedStatementNode::class.java)
        val numberOfTabs = 0

        val generatedStackItem = Mockito.mock(StatementPrinterStackItem::class.java)
        val generatedStackItem2 = Mockito.mock(StatementPrinterStackItem::class.java)
        Mockito.`when`(statementPrinterStackItemGenerator.generateStatementPrinterStackItems(node, numberOfTabs)).thenReturn(listOf(generatedStackItem, generatedStackItem2))

        val generatedNode2 = Mockito.mock(IParsedStatementNode::class.java)
        Mockito.`when`(generatedStackItem2.node).thenReturn(generatedNode2)
        Mockito.`when`(generatedStackItem2.location).thenReturn(PrinterConstants.LOCATION_2)

        val numberOfStatements2 = 0
        Mockito.`when`(generatedNode2.getNumberOfStatements()).thenReturn(numberOfStatements2)

        val generatedNumberOfTabs2 = 0
        Mockito.`when`(generatedStackItem2.numberOfTabs).thenReturn(generatedNumberOfTabs2)

        val result2 = "result2"
        Mockito.`when`(statementPrinterResultGenerator.generateResult(generatedNode2, generatedNumberOfTabs2, listOf())).thenReturn(result2)

        Mockito.`when`(generatedStackItem.location).thenReturn(PrinterConstants.LOCATION_2)

        val generatedNode = Mockito.mock(IParsedStatementNode::class.java)
        Mockito.`when`(generatedStackItem.node).thenReturn(generatedNode)

        val numberOfStatements = 1
        Mockito.`when`(generatedNode.getNumberOfStatements()).thenReturn(numberOfStatements)

        val generatedNumberOfTabs = 0
        Mockito.`when`(generatedStackItem.numberOfTabs).thenReturn(generatedNumberOfTabs)

        val expected = "result"
        Mockito.`when`(statementPrinterResultGenerator.generateResult(generatedNode, generatedNumberOfTabs, listOf(result2))).thenReturn(expected)

        val actual = statementPrinter.printNode(node, numberOfTabs)
        Assertions.assertEquals(expected, actual)
    }
}