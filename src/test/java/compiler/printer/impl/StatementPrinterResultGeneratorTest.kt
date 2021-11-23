package compiler.printer.impl

import compiler.core.*
import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IExpressionPrinter
import compiler.printer.impl.internal.IExpressionStatementPrinter
import compiler.printer.impl.internal.IReturnStatementPrinter
import compiler.printer.impl.internal.IVariableDeclarationListPrinter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class StatementPrinterResultGeneratorTest {
    private val variableDeclarationListPrinter = Mockito.mock(IVariableDeclarationListPrinter::class.java)
    private val returnStatementPrinter = Mockito.mock(IReturnStatementPrinter::class.java)
    private val expressionStatementPrinter = Mockito.mock(IExpressionStatementPrinter::class.java)
    private val expressionPrinter = Mockito.mock(IExpressionPrinter::class.java)

    private val statementPrinterResultGenerator = StatementPrinterResultGenerator(
        variableDeclarationListPrinter,
        returnStatementPrinter,
        expressionStatementPrinter,
        expressionPrinter
    )

    @Test
    fun printBasicBlockResultTest() {
        val node = Mockito.mock(ParsedBasicBlockNode::class.java)
        val numberOfTabs = 1
        val s1 = "s1"
        val s2 = "s2"
        val statementStrings = listOf(s1, s2)

        val expected = PrinterConstants.LEFT_BRACE +
                PrinterConstants.NEW_LINE + PrinterConstants.TAB + PrinterConstants.TAB +
                s1 +
                PrinterConstants.NEW_LINE + PrinterConstants.TAB + PrinterConstants.TAB +
                s2 +
                PrinterConstants.NEW_LINE + PrinterConstants.TAB +
                PrinterConstants.RIGHT_BRACE

        val actual = statementPrinterResultGenerator.generateResult(node, numberOfTabs, statementStrings)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun printDoWhileNodeTest() {
        val node = Mockito.mock(ParsedDoWhileNode::class.java)
        val numberOfTabs = 0
        val bodyString = "bodyString"
        val statementStrings = listOf(bodyString)

        val expression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.expression).thenReturn(expression)

        val expressionString = "expressionString"
        Mockito.`when`(expressionPrinter.printNode(expression)).thenReturn(expressionString)

        val expected = PrinterConstants.DO +
                PrinterConstants.SPACE +
                bodyString +
                PrinterConstants.SPACE +
                PrinterConstants.WHILE +
                PrinterConstants.SPACE +
                expressionString +
                PrinterConstants.SEMICOLON

        val actual = statementPrinterResultGenerator.generateResult(node, numberOfTabs, statementStrings)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun printWhileNodeTest() {
        val node = Mockito.mock(ParsedWhileNode::class.java)
        val numberOfTabs = 0
        val bodyString = "bodyString"
        val statementStrings = listOf(bodyString)

        val expression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.expression).thenReturn(expression)

        val expressionString = "expressionString"
        Mockito.`when`(expressionPrinter.printNode(expression)).thenReturn(expressionString)

        val expected = PrinterConstants.WHILE +
                PrinterConstants.SPACE +
                expressionString +
                PrinterConstants.SPACE +
                bodyString

        val actual = statementPrinterResultGenerator.generateResult(node, numberOfTabs, statementStrings)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun printForNodeTest() {
        val node = Mockito.mock(ParsedForNode::class.java)
        val numberOfTabs = 0
        val bodyString = "bodyString"
        val statementStrings = listOf(bodyString)

        val initExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.initExpression).thenReturn(initExpression)

        val initExpressionString = "initExpressionString"
        Mockito.`when`(expressionPrinter.printNode(initExpression)).thenReturn(initExpressionString)

        val testExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.testExpression).thenReturn(testExpression)

        val testExpressionString = "testExpressionString"
        Mockito.`when`(expressionPrinter.printNode(testExpression)).thenReturn(testExpressionString)

        val incrementExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.incrementExpression).thenReturn(incrementExpression)

        val incrementExpressionString = "incrementExpressionString"
        Mockito.`when`(expressionPrinter.printNode(incrementExpression)).thenReturn(incrementExpressionString)

        val expected = PrinterConstants.FOR +
                PrinterConstants.SPACE +
                PrinterConstants.LEFT_PARENTHESES +
                initExpressionString +
                PrinterConstants.SEMICOLON +
                PrinterConstants.SPACE +
                testExpressionString +
                PrinterConstants.SEMICOLON +
                PrinterConstants.SPACE +
                incrementExpressionString +
                PrinterConstants.RIGHT_PARENTHESES +
                PrinterConstants.SPACE +
                bodyString

        val actual = statementPrinterResultGenerator.generateResult(node, numberOfTabs, statementStrings)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun printIfNodeTest() {
        val node = Mockito.mock(ParsedIfNode::class.java)
        val numberOfTabs = 0
        val bodyString = "bodyString"
        val statementStrings = listOf(bodyString)

        val expression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.booleanExpression).thenReturn(expression)

        val expressionString = "expressionString"
        Mockito.`when`(expressionPrinter.printNode(expression)).thenReturn(expressionString)

        val expected = PrinterConstants.IF +
                expressionString +
                PrinterConstants.SPACE +
                bodyString

        val actual = statementPrinterResultGenerator.generateResult(node, numberOfTabs, statementStrings)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun printElseNodeTest() {
        val node = Mockito.mock(ParsedElseNode::class.java)
        val numberOfTabs = 0
        val bodyString = "bodyString"
        val statementStrings = listOf(bodyString)

        val expected = PrinterConstants.ELSE + PrinterConstants.SPACE + bodyString

        val actual = statementPrinterResultGenerator.generateResult(node, numberOfTabs, statementStrings)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun printVariableDeclarationListTest() {
        val node = Mockito.mock(ParsedVariableDeclarationListNode::class.java)
        val numberOfTabs = 0
        val statementStrings = listOf<String>()

        val expected = "result"
        Mockito.`when`(variableDeclarationListPrinter.printNode(node)).thenReturn(expected)

        val actual = statementPrinterResultGenerator.generateResult(node, numberOfTabs, statementStrings)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun printReturnDeclarationListTest() {
        val node = Mockito.mock(ParsedReturnNode::class.java)
        val numberOfTabs = 0
        val statementStrings = listOf<String>()

        val expected = "result"
        Mockito.`when`(returnStatementPrinter.printParsedNode(node)).thenReturn(expected)

        val actual = statementPrinterResultGenerator.generateResult(node, numberOfTabs, statementStrings)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun printExpressionStatementTest() {
        val node = Mockito.mock(ParsedExpressionStatementNode::class.java)
        val numberOfTabs = 0
        val statementStrings = listOf<String>()

        val expected = "result"
        Mockito.`when`(expressionStatementPrinter.printParsedNode(node)).thenReturn(expected)

        val actual = statementPrinterResultGenerator.generateResult(node, numberOfTabs, statementStrings)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun printNoMatchesTest() {
        val node = Mockito.mock(IParsedStatementNode::class.java)
        val numberOfTabs = 0
        val statementStrings = listOf<String>()

        val actual = statementPrinterResultGenerator.generateResult(node, numberOfTabs, statementStrings)
        Assertions.assertEquals(PrinterConstants.EMPTY, actual)
    }
}