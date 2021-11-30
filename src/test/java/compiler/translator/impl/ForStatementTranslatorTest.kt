package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedForNode
import compiler.core.nodes.translated.*
import compiler.core.stack.Stack
import compiler.core.stack.StatementTranslatorLocation
import compiler.core.stack.StatementTranslatorStackItem
import compiler.translator.impl.internal.IBooleanExpressionTranslatorOrchestrator
import compiler.translator.impl.internal.IExpressionTranslatorOrchestrator
import compiler.translator.impl.internal.ILabelGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ForStatementTranslatorTest {
    private val labelGenerator = Mockito.mock(ILabelGenerator::class.java)
    private val expressionTranslatorOrchestrator = Mockito.mock(IExpressionTranslatorOrchestrator::class.java)
    private val booleanExpressionTranslatorOrchestrator = Mockito.mock(IBooleanExpressionTranslatorOrchestrator::class.java)

    private val forStatementTranslator = ForStatementTranslator(
        labelGenerator,
        expressionTranslatorOrchestrator,
        booleanExpressionTranslatorOrchestrator
    )

    @Test
    fun startLocationTest() {
        val node = Mockito.mock(ParsedForNode::class.java)
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

        val beginLabel = "beginLabel"
        val labelAfterBegin = 5
        Mockito.`when`(labelGenerator.generateLabel(labelAfterFalse)).thenReturn(Pair(beginLabel, labelAfterBegin))

        val trueLabel = "trueLabel"
        val labelAfterTrue = 4
        Mockito.`when`(labelGenerator.generateLabel(labelAfterBegin)).thenReturn(Pair(trueLabel, labelAfterTrue))

        val parsedInitExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.initExpression).thenReturn(parsedInitExpression)

        val translatedInitExpression = Mockito.mock(TranslatedExpressionNode::class.java)
        val tempAfterInit = 22
        Mockito.`when`(expressionTranslatorOrchestrator.translate(parsedInitExpression, variableToTypeMap, tempCounter)).thenReturn(Pair(translatedInitExpression, tempAfterInit))

        val parsedTestExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.testExpression).thenReturn(parsedTestExpression)

        val translatedTestExpression = Mockito.mock(TranslatedBooleanExpressionNode::class.java)
        val labelAfterTest = 33
        val tempAfterTest = 44
        Mockito.`when`(booleanExpressionTranslatorOrchestrator.translate(parsedTestExpression, trueLabel, falseLabel, labelAfterTrue, tempAfterInit, variableToTypeMap)).thenReturn(Triple(translatedTestExpression, labelAfterTest, tempAfterTest))

        val parsedIncrementExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.incrementExpression).thenReturn(parsedIncrementExpression)

        val translatedIncrementExpression = Mockito.mock(TranslatedExpressionNode::class.java)
        val tempAfterIncrement = 55
        Mockito.`when`(expressionTranslatorOrchestrator.translate(parsedIncrementExpression, variableToTypeMap, tempAfterTest)).thenReturn(Pair(translatedIncrementExpression, tempAfterIncrement))

        val body = Mockito.mock(IParsedStatementNode::class.java)
        Mockito.`when`(node.body).thenReturn(body)

        val (actualTemp, actualLabel) = forStatementTranslator.translate(node, location, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, expressionStack, labelStack)
        Assertions.assertEquals(tempAfterIncrement, actualTemp)
        Assertions.assertEquals(labelAfterTest, actualLabel)

        val start = stack.pop()
        Assertions.assertEquals(StatementTranslatorStackItem(StatementTranslatorLocation.START, body), start)
        val end = stack.pop()
        Assertions.assertEquals(StatementTranslatorStackItem(StatementTranslatorLocation.END, node), end)

        val f = labelStack.pop()
        Assertions.assertEquals(falseLabel, f)
        val b = labelStack.pop()
        Assertions.assertEquals(beginLabel, b)
        val t = labelStack.pop()
        Assertions.assertEquals(trueLabel, t)

        val init = expressionStack.pop()
        Assertions.assertEquals(translatedInitExpression, init)
        val test = expressionStack.pop()
        Assertions.assertEquals(translatedTestExpression, test)
        val increment = expressionStack.pop()
        Assertions.assertEquals(translatedIncrementExpression, increment)
    }

    @Test
    fun endLocationTest() {
        val node = Mockito.mock(ParsedForNode::class.java)
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

        val beginLabel = "beginLabel"
        labelStack.push(beginLabel)

        val falseLabel = "falseLabel"
        labelStack.push(falseLabel)

        val incrementExpression = Mockito.mock(ITranslatedExpressionNode::class.java)
        expressionStack.push(incrementExpression)

        val testExpression = Mockito.mock(ITranslatedExpressionNode::class.java)
        expressionStack.push(testExpression)

        val initExpression = Mockito.mock(ITranslatedExpressionNode::class.java)
        expressionStack.push(initExpression)

        val body = Mockito.mock(ITranslatedStatementNode::class.java)
        resultStack.push(body)

        val expected = TranslatedForNode(
            initExpression,
            testExpression,
            incrementExpression,
            body,
            falseLabel,
            beginLabel,
            trueLabel
        )

        val (actualTemp, actualLabel) = forStatementTranslator.translate(node, location, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, expressionStack, labelStack)
        Assertions.assertEquals(tempCounter, actualTemp)
        Assertions.assertEquals(labelCounter, actualLabel)
        val actual = resultStack.pop()
        Assertions.assertEquals(expected, actual)
    }
}