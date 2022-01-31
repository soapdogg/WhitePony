package compiler.frontend.printer.impl

import compiler.core.nodes.translated.ITranslatedExpressionNode
import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.nodes.translated.TranslatedIfNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import compiler.frontend.printer.impl.internal.ICodeGenerator
import compiler.frontend.printer.impl.internal.IGotoCodeGenerator
import compiler.frontend.printer.impl.internal.ILabelCodeGenerator
import compiler.frontend.printer.impl.internal.IStatementPrinterStackPusher
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class TranslatedIfStatementPrinterTest {
    private val statementPrinterStackPusher = Mockito.mock(IStatementPrinterStackPusher::class.java)
    private val codeGenerator = Mockito.mock(ICodeGenerator::class.java)
    private val labelCodeGenerator = Mockito.mock(ILabelCodeGenerator::class.java)
    private val gotoCodeGenerator = Mockito.mock(IGotoCodeGenerator::class.java)

    private val translatedIfStatementPrinter = TranslatedIfStatementPrinter(
        statementPrinterStackPusher,
        codeGenerator,
        labelCodeGenerator,
        gotoCodeGenerator
    )

    @Test
    fun startLocationElseNotPresentTest() {
        val node = Mockito.mock(TranslatedIfNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.START
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        val appendSemicolon = true

        val ifBody = Mockito.mock(ITranslatedStatementNode::class.java)
        Mockito.`when`(node.ifBody).thenReturn(ifBody)

        translatedIfStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, appendSemicolon)
        Mockito.verify(statementPrinterStackPusher).push(node, numberOfTabs, StatementPrinterLocation.END, stack)
        Mockito.verify(statementPrinterStackPusher).push(ifBody, numberOfTabs, StatementPrinterLocation.START, stack)
    }

    @Test
    fun startLocationElsePresentTest() {
        val node = Mockito.mock(TranslatedIfNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.START
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        val appendSemicolon = true

        val ifBody = Mockito.mock(ITranslatedStatementNode::class.java)
        Mockito.`when`(node.ifBody).thenReturn(ifBody)

        val elseBody = Mockito.mock(ITranslatedStatementNode::class.java)
        Mockito.`when`(node.elseBody).thenReturn(elseBody)

        translatedIfStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, appendSemicolon)
        Mockito.verify(statementPrinterStackPusher).push(node, numberOfTabs, StatementPrinterLocation.END, stack)
        Mockito.verify(statementPrinterStackPusher).push(ifBody, numberOfTabs, StatementPrinterLocation.START, stack)
        Mockito.verify(statementPrinterStackPusher).push(elseBody, numberOfTabs, StatementPrinterLocation.START, stack)
    }

    @Test
    fun endLocationElseNotPresentTest() {
        val node = Mockito.mock(TranslatedIfNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.END
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        val appendSemicolon = true

        val ifBodyCode = "ifBodyCode"
        resultStack.push(ifBodyCode)

        val expression = Mockito.mock(ITranslatedExpressionNode::class.java)
        Mockito.`when`(node.expression).thenReturn(expression)

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

        val ifStatementCode = listOf(
            expressionCode,
            trueLabelCode,
            ifBodyCode,
            falseLabelCode
        )
        val result = "result"
        Mockito.`when`(codeGenerator.generateCode(ifStatementCode)).thenReturn(result)

        translatedIfStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, appendSemicolon)
        val top = resultStack.pop()
        Assertions.assertEquals(result, top)
    }

    @Test
    fun endLocationElsePresentTest() {
        val node = Mockito.mock(TranslatedIfNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.END
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()
        val appendSemicolon = true

        val elseBodyCode = "elseBodyCode"
        resultStack.push(elseBodyCode)

        val ifBodyCode = "ifBodyCode"
        resultStack.push(ifBodyCode)

        val expression = Mockito.mock(ITranslatedExpressionNode::class.java)
        Mockito.`when`(node.expression).thenReturn(expression)

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

        val elseBody = Mockito.mock(ITranslatedStatementNode::class.java)
        Mockito.`when`(node.elseBody).thenReturn(elseBody)

        val nextLabel = "nextLabel"
        Mockito.`when`(node.nextLabel).thenReturn(nextLabel)

        val nextLabelCode = "nextLabelCode"
        Mockito.`when`(labelCodeGenerator.generateLabelCode(nextLabel)).thenReturn(nextLabelCode)

        val gotoNextCode = "gotoNextCode"
        Mockito.`when`(gotoCodeGenerator.generateGotoCode(nextLabel)).thenReturn(gotoNextCode)

        val ifStatementCode = listOf(
            expressionCode,
            trueLabelCode,
            ifBodyCode,
            gotoNextCode,
            falseLabelCode,
            elseBodyCode,
            nextLabelCode
        )
        val result = "result"
        Mockito.`when`(codeGenerator.generateCode(ifStatementCode)).thenReturn(result)

        translatedIfStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack, appendSemicolon)
        val top = resultStack.pop()
        Assertions.assertEquals(result, top)
    }
}