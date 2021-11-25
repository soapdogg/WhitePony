package compiler.printer.impl

import compiler.core.*
import compiler.core.constants.PrinterConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.ArgumentsAccessor
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito
import java.util.stream.Stream

class StatementPrinterStackItemGeneratorTest {

    private val statementPrinterStackItemGenerator = StatementPrinterStackItemGenerator()

    @ParameterizedTest
    @MethodSource("inputData")
    fun generateStatementPrinterStackItemTest(arguments: ArgumentsAccessor) {
        val triple = arguments.get(0) as Triple<*,*, *>
        val inputNode = triple.first as IParsedStatementNode
        val numberOfTabs = triple.second as Int
        val actual = statementPrinterStackItemGenerator.generateStatementPrinterStackItems(inputNode, numberOfTabs)

        val first = actual[0]
        Assertions.assertEquals(inputNode, first.node)
        Assertions.assertEquals(numberOfTabs, first.numberOfTabs)
        Assertions.assertEquals(PrinterConstants.LOCATION_2, first.location)
        if (triple.third == null) {
            Assertions.assertEquals(1, actual.size)
        } else {
            val expectedSecondItem = triple.third as StatementPrinterStackItem
            val second = actual[1]
            Assertions.assertEquals(expectedSecondItem.node, second.node)
            Assertions.assertEquals(expectedSecondItem.numberOfTabs, second.numberOfTabs)
            Assertions.assertEquals(expectedSecondItem.location, second.location)
        }
    }

    companion object {
        @JvmStatic
        fun inputData(): Stream<Triple<IParsedStatementNode, Int, StatementPrinterStackItem?>> {
            val numberOfTabs = 0

            val basicBlockNode = Mockito.mock(ParsedBasicBlockNode::class.java)
            val basicBlockStatement = Mockito.mock(IParsedStatementNode::class.java)
            Mockito.`when`(basicBlockNode.statements).thenReturn(listOf(basicBlockStatement))
            val basicBlockPrinterStackItem = StatementPrinterStackItem(basicBlockStatement, numberOfTabs + 1, PrinterConstants.LOCATION_1)

            val doWhileNode = Mockito.mock(ParsedDoWhileNode::class.java)
            val doWhileBody = Mockito.mock(IParsedStatementNode::class.java)
            Mockito.`when`(doWhileNode.body).thenReturn(doWhileBody)
            val doWhilePrinterStackItem = StatementPrinterStackItem(doWhileBody, numberOfTabs, PrinterConstants.LOCATION_1)

            val whileNode = Mockito.mock(ParsedWhileNode::class.java)
            val whileBody = Mockito.mock(IParsedStatementNode::class.java)
            Mockito.`when`(whileNode.body).thenReturn(whileBody)
            val whilePrinterStackItem = StatementPrinterStackItem(whileBody, numberOfTabs, PrinterConstants.LOCATION_1)

            val forNode = Mockito.mock(ParsedForNode::class.java)
            val forBody = Mockito.mock(IParsedStatementNode::class.java)
            Mockito.`when`(forNode.body).thenReturn(forBody)
            val forPrinterStackItem = StatementPrinterStackItem(forBody, numberOfTabs, PrinterConstants.LOCATION_1)

            val ifNode = Mockito.mock(ParsedIfNode::class.java)
            val ifBody = Mockito.mock(IParsedStatementNode::class.java)
            Mockito.`when`(ifNode.ifBody).thenReturn(ifBody)
            val ifPrinterStackItem = StatementPrinterStackItem(ifBody, numberOfTabs, PrinterConstants.LOCATION_1)

            return Stream.of(
                Triple(Mockito.mock(IParsedStatementNode::class.java), numberOfTabs, null),
                Triple(basicBlockNode, numberOfTabs, basicBlockPrinterStackItem),
                Triple(doWhileNode, numberOfTabs, doWhilePrinterStackItem),
                Triple(whileNode, numberOfTabs, whilePrinterStackItem),
                Triple(forNode, numberOfTabs, forPrinterStackItem),
                Triple(ifNode, numberOfTabs, ifPrinterStackItem),
            )
        }
    }
}