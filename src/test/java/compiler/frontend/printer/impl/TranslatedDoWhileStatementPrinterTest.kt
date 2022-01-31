package compiler.frontend.printer.impl

import compiler.core.nodes.translated.ITranslatedExpressionNode
import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.nodes.translated.TranslatedDoWhileNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.frontend.printer.impl.internal.ICodeGenerator
import compiler.frontend.printer.impl.internal.ILabelCodeGenerator
import compiler.frontend.printer.impl.internal.IStatementPrinterStackPusher
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class TranslatedDoWhileStatementPrinterTest {
    private val statementPrinterStackPusher = Mockito.mock(IStatementPrinterStackPusher::class.java)
    private val codeGenerator = Mockito.mock(ICodeGenerator::class.java)
    private val labelCodeGenerator = Mockito.mock(ILabelCodeGenerator::class.java)

    private val translatedDoWhileStatementPrinter = TranslatedDoWhileStatementPrinter(
        statementPrinterStackPusher,
        codeGenerator,
        labelCodeGenerator
    )

    @Test
    fun startLocationTest() {
        val node = Mockito.mock(TranslatedDoWhileNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.START
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        val appendSemicolon = true

        val body = Mockito.mock(ITranslatedStatementNode::class.java)
        Mockito.`when`(node.body).thenReturn(body)

        translatedDoWhileStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, appendSemicolon)
        Mockito.verify(statementPrinterStackPusher).push(node, numberOfTabs, StatementPrinterLocation.END, stack)
        Mockito.verify(statementPrinterStackPusher).push(body, numberOfTabs, StatementPrinterLocation.START, stack)
    }

    @Test
    fun endLocationTest() {
        val node = Mockito.mock(TranslatedDoWhileNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.END
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        val appendSemicolon = true

        val bodyStatementCode = "bodyStatementCode"
        resultStack.push(bodyStatementCode)

        val expression = Mockito.mock(ITranslatedExpressionNode::class.java)
        Mockito.`when`(node.expressionNode).thenReturn(expression)

        val expressionCode = "expressionCode"
        Mockito.`when`(expression.code).thenReturn(listOf(expressionCode))
        Mockito.`when`(codeGenerator.generateCode(listOf(expressionCode))).thenReturn(expressionCode)

        val trueLabel = "trueLabel"
        Mockito.`when`(node.trueLabel).thenReturn(trueLabel)

        val trueLabelCode= "trueLabelCode"
        Mockito.`when`(labelCodeGenerator.generateLabelCode(trueLabel)).thenReturn(trueLabelCode)

        val falseLabel = "falseLabel"
        Mockito.`when`(node.falseLabel).thenReturn(falseLabel)

        val falseLabelCode = "falseLabelCode"
        Mockito.`when`(labelCodeGenerator.generateLabelCode(falseLabel)).thenReturn(falseLabelCode)

        val doWhileStatementCode = listOf(
            trueLabelCode,
            bodyStatementCode,
            expressionCode,
            falseLabelCode
        )
        val result = "result"
        Mockito.`when`(codeGenerator.generateCode(doWhileStatementCode)).thenReturn(result)

        translatedDoWhileStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, appendSemicolon)
        val top = resultStack.pop()
        Assertions.assertEquals(result, top)
    }
}