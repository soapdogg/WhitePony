package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedIfNode
import compiler.core.nodes.translated.*
import compiler.core.stack.Stack
import compiler.core.stack.StatementTranslatorLocation
import compiler.core.stack.StatementTranslatorStackItem
import compiler.translator.impl.internal.IBooleanExpressionTranslatorOrchestrator
import compiler.translator.impl.internal.ILabelGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class IfStatementTranslatorTest {
    private val labelGenerator = Mockito.mock(ILabelGenerator::class.java)
    private val booleanExpressionTranslatorOrchestrator = Mockito.mock(IBooleanExpressionTranslatorOrchestrator::class.java)

    private val ifStatementTranslator = IfStatementTranslator(
        labelGenerator,
        booleanExpressionTranslatorOrchestrator
    )

    @Test
    fun startLocationElseNotPresentTest(){
        val node = Mockito.mock(ParsedIfNode::class.java)
        val location = StatementTranslatorLocation.START
        val tempCounter = 1
        val labelCounter = 2
        val variableToTypeMap = mutableMapOf<String,String>()
        val stack = Stack<StatementTranslatorStackItem>()
        val resultStack = Stack<ITranslatedStatementNode>()
        val expressionStack = Stack<ITranslatedExpressionNode>()
        val labelStack = Stack<String>()

        val nextLabel = "nextLabel"
        val labelAfterNext = 3
        Mockito.`when`(labelGenerator.generateLabel(labelCounter)).thenReturn(Pair(nextLabel, labelAfterNext))

        val trueLabel = "trueLabel"
        val labelAfterTrue = 4
        Mockito.`when`(labelGenerator.generateLabel(labelAfterNext)).thenReturn(Pair(trueLabel, labelAfterTrue))

        val parsedExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.booleanExpression).thenReturn(parsedExpression)

        val translatedExpression = Mockito.mock(TranslatedBooleanExpressionNode::class.java)
        val labelAfterExpression = 44
        val tempAfterExpression = 55
        Mockito.`when`(booleanExpressionTranslatorOrchestrator.translate(parsedExpression, trueLabel, nextLabel, labelAfterTrue, tempCounter, variableToTypeMap)).thenReturn(Triple(translatedExpression, labelAfterExpression, tempAfterExpression))

        val ifBody = Mockito.mock(IParsedStatementNode::class.java)
        Mockito.`when`(node.ifBody).thenReturn(ifBody)

        val (actualTemp, actualLabel) = ifStatementTranslator.translate(node, location, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, expressionStack, labelStack)
        Assertions.assertEquals(tempAfterExpression, actualTemp)
        Assertions.assertEquals(labelAfterExpression, actualLabel)

        val start = stack.pop()
        Assertions.assertEquals(StatementTranslatorStackItem(StatementTranslatorLocation.START, ifBody), start)
        val end = stack.pop()
        Assertions.assertEquals(StatementTranslatorStackItem(StatementTranslatorLocation.END, node), end)

        val n = labelStack.pop()
        Assertions.assertEquals(nextLabel, n)
        val t = labelStack.pop()
        Assertions.assertEquals(trueLabel, t)

        val e = expressionStack.pop()
        Assertions.assertEquals(translatedExpression, e)
    }

    @Test
    fun startLocationElsePresentTest(){
        val node = Mockito.mock(ParsedIfNode::class.java)
        val location = StatementTranslatorLocation.START
        val tempCounter = 1
        val labelCounter = 2
        val variableToTypeMap = mutableMapOf<String,String>()
        val stack = Stack<StatementTranslatorStackItem>()
        val resultStack = Stack<ITranslatedStatementNode>()
        val expressionStack = Stack<ITranslatedExpressionNode>()
        val labelStack = Stack<String>()

        val nextLabel = "nextLabel"
        val labelAfterNext = 3
        Mockito.`when`(labelGenerator.generateLabel(labelCounter)).thenReturn(Pair(nextLabel, labelAfterNext))

        val trueLabel = "trueLabel"
        val labelAfterTrue = 4
        Mockito.`when`(labelGenerator.generateLabel(labelAfterNext)).thenReturn(Pair(trueLabel, labelAfterTrue))

        val elseBody = Mockito.mock(IParsedStatementNode::class.java)
        Mockito.`when`(node.elseBody).thenReturn(elseBody)

        val parsedExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.booleanExpression).thenReturn(parsedExpression)

        val falseLabel = "falseLabel"
        val labelAfterFalse = 5
        Mockito.`when`(labelGenerator.generateLabel(labelAfterTrue)).thenReturn(Pair(falseLabel, labelAfterFalse))

        val translatedExpression = Mockito.mock(TranslatedBooleanExpressionNode::class.java)
        val labelAfterExpression = 44
        val tempAfterExpression = 55
        Mockito.`when`(booleanExpressionTranslatorOrchestrator.translate(parsedExpression, trueLabel, falseLabel, labelAfterFalse, tempCounter, variableToTypeMap)).thenReturn(Triple(translatedExpression, labelAfterExpression, tempAfterExpression))

        val ifBody = Mockito.mock(IParsedStatementNode::class.java)
        Mockito.`when`(node.ifBody).thenReturn(ifBody)

        val (actualTemp, actualLabel) = ifStatementTranslator.translate(node, location, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, expressionStack, labelStack)
        Assertions.assertEquals(tempAfterExpression, actualTemp)
        Assertions.assertEquals(labelAfterExpression, actualLabel)

        val start = stack.pop()
        Assertions.assertEquals(StatementTranslatorStackItem(StatementTranslatorLocation.START, ifBody), start)
        val elseStart = stack.pop()
        Assertions.assertEquals(StatementTranslatorStackItem(StatementTranslatorLocation.START, elseBody), elseStart)
        val end = stack.pop()
        Assertions.assertEquals(StatementTranslatorStackItem(StatementTranslatorLocation.END, node), end)

        val n = labelStack.pop()
        Assertions.assertEquals(nextLabel, n)
        val t = labelStack.pop()
        Assertions.assertEquals(trueLabel, t)
        val f = labelStack.pop()
        Assertions.assertEquals(falseLabel, f)

        val e = expressionStack.pop()
        Assertions.assertEquals(translatedExpression, e)
    }

    @Test
    fun endLocationElseNotPresentTest() {
        val node = Mockito.mock(ParsedIfNode::class.java)
        val location = StatementTranslatorLocation.END
        val tempCounter = 1
        val labelCounter = 2
        val variableToTypeMap = mutableMapOf<String,String>()
        val stack = Stack<StatementTranslatorStackItem>()
        val resultStack = Stack<ITranslatedStatementNode>()
        val expressionStack = Stack<ITranslatedExpressionNode>()
        val labelStack = Stack<String>()

        val trueLabel = "trueLabel"
        labelStack.push(trueLabel)

        val nextLabel = "nextLabel"
        labelStack.push(nextLabel)

        val expression = Mockito.mock(ITranslatedExpressionNode::class.java)
        expressionStack.push(expression)

        val body = Mockito.mock(ITranslatedStatementNode::class.java)
        resultStack.push(body)

        val expected = TranslatedIfNode(
            expression,
            body,
            null,
            nextLabel,
            trueLabel,
            nextLabel
        )

        val (actualTemp, actualLabel) = ifStatementTranslator.translate(node, location, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, expressionStack, labelStack)
        Assertions.assertEquals(tempCounter, actualTemp)
        Assertions.assertEquals(labelCounter, actualLabel)
        val actual = resultStack.pop()
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun endLocationElsePresentTest() {
        val node = Mockito.mock(ParsedIfNode::class.java)
        val location = StatementTranslatorLocation.END
        val tempCounter = 1
        val labelCounter = 2
        val variableToTypeMap = mutableMapOf<String,String>()
        val stack = Stack<StatementTranslatorStackItem>()
        val resultStack = Stack<ITranslatedStatementNode>()
        val expressionStack = Stack<ITranslatedExpressionNode>()
        val labelStack = Stack<String>()

        val falseLabel = "falseLabel"
        labelStack.push(falseLabel)

        val trueLabel = "trueLabel"
        labelStack.push(trueLabel)

        val nextLabel = "nextLabel"
        labelStack.push(nextLabel)

        val expression = Mockito.mock(ITranslatedExpressionNode::class.java)
        expressionStack.push(expression)

        val ifBody = Mockito.mock(ITranslatedStatementNode::class.java)
        resultStack.push(ifBody)

        val elseBody = Mockito.mock(IParsedStatementNode::class.java)
        Mockito.`when`(node.elseBody).thenReturn(elseBody)

        val body = Mockito.mock(ITranslatedStatementNode::class.java)
        resultStack.push(body)

        val expected = TranslatedIfNode(
            expression,
            ifBody,
            body,
            nextLabel,
            trueLabel,
            falseLabel
        )

        val (actualTemp, actualLabel) = ifStatementTranslator.translate(node, location, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, expressionStack, labelStack)
        Assertions.assertEquals(tempCounter, actualTemp)
        Assertions.assertEquals(labelCounter, actualLabel)
        val actual = resultStack.pop()
        Assertions.assertEquals(expected, actual)
    }
}