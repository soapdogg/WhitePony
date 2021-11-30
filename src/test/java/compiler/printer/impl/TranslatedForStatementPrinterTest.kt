package compiler.printer.impl

import compiler.core.nodes.translated.ITranslatedExpressionNode
import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.nodes.translated.TranslatedForNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.printer.impl.internal.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class TranslatedForStatementPrinterTest {
    private val statementPrinterStackPusher = Mockito.mock(IStatementPrinterStackPusher::class.java)
    private val codeGenerator = Mockito.mock(ICodeGenerator::class.java)
    private val labelCodeGenerator = Mockito.mock(ILabelCodeGenerator::class.java)
    private val gotoCodeGenerator = Mockito.mock(IGotoCodeGenerator::class.java)

    private val translatedForStatementPrinter = TranslatedForStatementPrinter(
        statementPrinterStackPusher,
        codeGenerator,
        labelCodeGenerator,
        gotoCodeGenerator
    )

    @Test
    fun startLocationTest() {
        val node = Mockito.mock(TranslatedForNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.START
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        val appendSemicolon = false

        val body = Mockito.mock(ITranslatedStatementNode::class.java)
        Mockito.`when`(node.body).thenReturn(body)

        translatedForStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, appendSemicolon)

        Mockito.verify(statementPrinterStackPusher).push(node, numberOfTabs, StatementPrinterLocation.END, stack)
        Mockito.verify(statementPrinterStackPusher).push(body, numberOfTabs, StatementPrinterLocation.START, stack)
    }

    @Test
    fun endForLocationTest() {
        val node = Mockito.mock(TranslatedForNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.END
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        val appendSemicolon = false

        val bodyStatementString = "bodyStatementString"
        resultStack.push(bodyStatementString)

        val initExpression = Mockito.mock(ITranslatedExpressionNode::class.java)
        Mockito.`when`(node.initExpression).thenReturn(initExpression)

        val initExpressionCode = "initExpressionCode"
        Mockito.`when`(initExpression.code).thenReturn(listOf(initExpressionCode))
        Mockito.`when`(codeGenerator.generateCode(listOf(initExpressionCode))).thenReturn(initExpressionCode)

        val testExpression = Mockito.mock(ITranslatedExpressionNode::class.java)
        Mockito.`when`(node.testExpression).thenReturn(testExpression)

        val testExpressionCode = "testExpressionCode"
        Mockito.`when`(testExpression.code).thenReturn(listOf(testExpressionCode))
        Mockito.`when`(codeGenerator.generateCode(listOf(testExpressionCode))).thenReturn(testExpressionCode)

        val incrementExpression = Mockito.mock(ITranslatedExpressionNode::class.java)
        Mockito.`when`(node.incrementExpression).thenReturn(incrementExpression)

        val incrementExpressionCode = "incrementExpressionCode"
        Mockito.`when`(incrementExpression.code).thenReturn(listOf(incrementExpressionCode))
        Mockito.`when`(codeGenerator.generateCode(listOf(incrementExpressionCode))).thenReturn(incrementExpressionCode)

        val beginLabel = "beginLabel"
        Mockito.`when`(node.beginLabel).thenReturn(beginLabel)

        val beginLabelCode = "beginLabelCode"
        Mockito.`when`(labelCodeGenerator.generateLabelCode(beginLabel)).thenReturn(beginLabelCode)

        val trueLabel = "trueLabel"
        Mockito.`when`(node.trueLabel).thenReturn(trueLabel)

        val trueLabelCode = "trueLabelCode"
        Mockito.`when`(labelCodeGenerator.generateLabelCode(trueLabel)).thenReturn(trueLabelCode)

        val falseLabel = "falseLabel"
        Mockito.`when`(node.falseLabel).thenReturn(falseLabel)

        val falseLabelCode = "falseLabelCode"
        Mockito.`when`(labelCodeGenerator.generateLabelCode(falseLabel)).thenReturn(falseLabelCode)

        val gotoBeginCode = "gotoBeginCode"
        Mockito.`when`(gotoCodeGenerator.generateGotoCode(beginLabel)).thenReturn(gotoBeginCode)

        val forStatementCode = listOf(
            initExpressionCode,
            beginLabelCode,
            testExpressionCode,
            trueLabelCode,
            bodyStatementString,
            incrementExpressionCode,
            gotoBeginCode,
            falseLabelCode
        )
        val result = "result"
        Mockito.`when`(codeGenerator.generateCode(forStatementCode)).thenReturn(result)

        translatedForStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, appendSemicolon)
        val actual = resultStack.pop()
        Assertions.assertEquals(result, actual)
    }

}