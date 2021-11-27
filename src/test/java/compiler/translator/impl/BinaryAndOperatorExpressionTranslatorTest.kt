package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryAndOperatorExpressionNode
import compiler.core.nodes.translated.TranslatedBooleanExpressionNode
import compiler.core.stack.BooleanExpressionTranslatorStackItem
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IBooleanExpressionTranslatorStackPusher
import compiler.translator.impl.internal.ILabelCodeGenerator
import compiler.translator.impl.internal.ILabelGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class BinaryAndOperatorExpressionTranslatorTest {
    private val labelGenerator = Mockito.mock(ILabelGenerator::class.java)
    private val booleanExpressionTranslatorStackPusher = Mockito.mock(IBooleanExpressionTranslatorStackPusher::class.java)
    private val labelCodeGenerator = Mockito.mock(ILabelCodeGenerator::class.java)

    private val binaryAndOperatorExpressionTranslator = BinaryAndOperatorExpressionTranslator(
        labelGenerator,
        booleanExpressionTranslatorStackPusher,
        labelCodeGenerator
    )

    @Test
    fun location1Test() {
        val node = Mockito.mock(ParsedBinaryAndOperatorExpressionNode::class.java)
        val location = LocationConstants.LOCATION_1
        val trueLabel = "true"
        val falseLabel = "false"
        val tempCounter = 1
        val labelCounter = 2
        val variableToTypeMap = mapOf<String, String>()
        val stack = Stack<BooleanExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedBooleanExpressionNode>()
        val labelStack = Stack<String>()

        val tLabel = "t"
        val l = 3
        Mockito.`when`(labelGenerator.generateLabel(labelCounter)).thenReturn(Pair(tLabel, l))

        val leftExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(leftExpression)

        val (ll, t) = binaryAndOperatorExpressionTranslator.translate(
            node, location, trueLabel, falseLabel, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, labelStack
        )

        Assertions.assertEquals(l, ll)
        Assertions.assertEquals(tempCounter, t)
        val topLabel = labelStack.pop()
        Assertions.assertEquals(tLabel, topLabel)
        Mockito.verify(booleanExpressionTranslatorStackPusher).push(LocationConstants.LOCATION_2, node, trueLabel, falseLabel, stack)
        Mockito.verify(booleanExpressionTranslatorStackPusher).push(LocationConstants.LOCATION_1, leftExpression, tLabel, falseLabel, stack)
    }

    @Test
    fun location2Test() {
        val node = Mockito.mock(ParsedBinaryAndOperatorExpressionNode::class.java)
        val location = LocationConstants.LOCATION_2
        val trueLabel = "true"
        val falseLabel = "false"
        val tempCounter = 1
        val labelCounter = 2
        val variableToTypeMap = mapOf<String, String>()
        val stack = Stack<BooleanExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedBooleanExpressionNode>()
        val labelStack = Stack<String>()

        val rightExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.rightExpression).thenReturn(rightExpression)

        val (l, t) = binaryAndOperatorExpressionTranslator.translate(
            node, location, trueLabel, falseLabel, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, labelStack
        )

        Assertions.assertEquals(labelCounter, l)
        Assertions.assertEquals(tempCounter, t)
        Mockito.verify(booleanExpressionTranslatorStackPusher).push(LocationConstants.LOCATION_3, node, trueLabel, falseLabel, stack)
        Mockito.verify(booleanExpressionTranslatorStackPusher).push(LocationConstants.LOCATION_1, rightExpression, trueLabel, falseLabel, stack)
    }

    @Test
    fun location3Test() {
        val node = Mockito.mock(ParsedBinaryAndOperatorExpressionNode::class.java)
        val location = LocationConstants.LOCATION_3
        val trueLabel = "true"
        val falseLabel = "false"
        val tempCounter = 1
        val labelCounter = 2
        val variableToTypeMap = mapOf<String, String>()
        val stack = Stack<BooleanExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedBooleanExpressionNode>()
        val labelStack = Stack<String>()

        val leftExpression = Mockito.mock(TranslatedBooleanExpressionNode::class.java)
        resultStack.push(leftExpression)

        val rightExpression = Mockito.mock(TranslatedBooleanExpressionNode::class.java)
        resultStack.push(rightExpression)

        val tLabel = "tLabel"
        labelStack.push(tLabel)

        val labelCode = "labelCode"
        Mockito.`when`(labelCodeGenerator.generateLabelCode(tLabel)).thenReturn(labelCode)

        val leftCode = "leftCode"
        Mockito.`when`(leftExpression.code).thenReturn(listOf(leftCode))

        val rightCode = "rightCode"
        Mockito.`when`(rightExpression.code).thenReturn(listOf(rightCode))

        val (l, t) = binaryAndOperatorExpressionTranslator.translate(
            node, location, trueLabel, falseLabel, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, labelStack
        )

        Assertions.assertEquals(labelCounter, l)
        Assertions.assertEquals(tempCounter, t)
        val top = resultStack.pop()
        Assertions.assertEquals(listOf(leftCode, labelCode, rightCode), top.code)
    }

    @Test
    fun location0Test() {
        val node = Mockito.mock(ParsedBinaryAndOperatorExpressionNode::class.java)
        val location = 0
        val trueLabel = "true"
        val falseLabel = "false"
        val tempCounter = 1
        val labelCounter = 2
        val variableToTypeMap = mapOf<String, String>()
        val stack = Stack<BooleanExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedBooleanExpressionNode>()
        val labelStack = Stack<String>()

        val (l, t) = binaryAndOperatorExpressionTranslator.translate(
            node, location, trueLabel, falseLabel, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, labelStack
        )

        Assertions.assertEquals(labelCounter, l)
        Assertions.assertEquals(tempCounter, t)
    }
}