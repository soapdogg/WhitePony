package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedDoWhileNode
import compiler.core.nodes.translated.ITranslatedExpressionNode
import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.nodes.translated.TranslatedBooleanExpressionNode
import compiler.core.nodes.translated.TranslatedDoWhileNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementTranslatorLocation
import compiler.core.stack.StatementTranslatorStackItem
import compiler.translator.impl.internal.IBooleanExpressionTranslatorOrchestrator
import compiler.translator.impl.internal.ILabelGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class DoWhileStatementTranslatorTest {
    private val labelGenerator = Mockito.mock(ILabelGenerator::class.java)
    private val booleanExpressionTranslatorOrchestrator = Mockito.mock(IBooleanExpressionTranslatorOrchestrator::class.java)

    private val doWhileStatementTranslator = DoWhileStatementTranslator(
        labelGenerator,
        booleanExpressionTranslatorOrchestrator
    )

    @Test
    fun startLocationTest() {
        val node = Mockito.mock(ParsedDoWhileNode::class.java)
        val location = StatementTranslatorLocation.START
        val tempCounter = 1
        val labelCounter = 2
        val variableToTypeMap = mapOf<String,String>()
        val stack = Stack<StatementTranslatorStackItem>()
        val resultStack = Stack<ITranslatedStatementNode>()
        val expressionStack = Stack<ITranslatedExpressionNode>()
        val labelStack = Stack<String>()

        val falseLabel = "falseLabel"
        val labelAfterFalse = 3
        Mockito.`when`(labelGenerator.generateLabel(labelCounter)).thenReturn(Pair(falseLabel, labelAfterFalse))

        val trueLabel = "trueLabel"
        val labelAfterTrue = 4
        Mockito.`when`(labelGenerator.generateLabel(labelAfterFalse)).thenReturn(Pair(trueLabel, labelAfterTrue))

        val body = Mockito.mock(IParsedStatementNode::class.java)
        Mockito.`when`(node.body).thenReturn(body)

        val (actualTemp, actualLabel) = doWhileStatementTranslator.translate(node, location, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, expressionStack, labelStack)
        Assertions.assertEquals(tempCounter, actualTemp)
        Assertions.assertEquals(labelAfterTrue, actualLabel)
        val start = stack.pop()
        Assertions.assertEquals(StatementTranslatorStackItem(StatementTranslatorLocation.START, body), start)
        val end = stack.pop()
        Assertions.assertEquals(StatementTranslatorStackItem(StatementTranslatorLocation.END, node), end)
        val f = labelStack.pop()
        Assertions.assertEquals(falseLabel, f)
        val t = labelStack.pop()
        Assertions.assertEquals(trueLabel, t)
    }

    @Test
    fun endLocationTest() {
        val node = Mockito.mock(ParsedDoWhileNode::class.java)
        val location = StatementTranslatorLocation.END
        val tempCounter = 1
        val labelCounter = 2
        val variableToTypeMap = mapOf<String,String>()
        val stack = Stack<StatementTranslatorStackItem>()
        val resultStack = Stack<ITranslatedStatementNode>()
        val expressionStack = Stack<ITranslatedExpressionNode>()
        val labelStack = Stack<String>()

        val trueLabel = "trueLabel"
        labelStack.push(trueLabel)

        val falseLabel = "falseLabel"
        labelStack.push(falseLabel)

        val parsedExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.expression).thenReturn(parsedExpression)

        val translatedExpression = Mockito.mock(TranslatedBooleanExpressionNode::class.java)
        val labelCountAfterExpression = 3
        val tempCountAfterExpression = 4
        Mockito.`when`(
            booleanExpressionTranslatorOrchestrator.translate(
                parsedExpression,
                trueLabel,
                falseLabel,
                labelCounter,
                tempCounter,
                variableToTypeMap
            )
        ).thenReturn(Triple(translatedExpression, labelCountAfterExpression, tempCountAfterExpression))

        val body = Mockito.mock(ITranslatedStatementNode::class.java)
        resultStack.push(body)

        val (actualTemp, actualLabel) =  doWhileStatementTranslator.translate(node, location, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, expressionStack, labelStack)
        Assertions.assertEquals(tempCountAfterExpression, actualTemp)
        Assertions.assertEquals(labelCountAfterExpression, actualLabel)
        val top = resultStack.pop()
        val expected = TranslatedDoWhileNode(
            translatedExpression,
            body,
            falseLabel,
            trueLabel
        )
        Assertions.assertEquals(expected, top)
    }
}