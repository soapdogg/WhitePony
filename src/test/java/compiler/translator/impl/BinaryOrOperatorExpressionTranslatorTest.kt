package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryOrOperatorExpressionNode
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

class BinaryOrOperatorExpressionTranslatorTest {
    private val labelGenerator = Mockito.mock(ILabelGenerator::class.java)
    private val booleanExpressionTranslatorStackPusher = Mockito.mock(IBooleanExpressionTranslatorStackPusher::class.java)
    private val labelCodeGenerator = Mockito.mock(ILabelCodeGenerator::class.java)

    private val binaryOrOperatorExpressionTranslator = BinaryOrOperatorExpressionTranslator(
        labelGenerator,
        booleanExpressionTranslatorStackPusher,
        labelCodeGenerator
    )

    @Test
    fun location1Test() {
        val node = Mockito.mock(ParsedBinaryOrOperatorExpressionNode::class.java)
        val location = LocationConstants.LOCATION_1
        val trueLabel = "true"
        val falseLabel = "false"
        val tempCounter = 1
        val labelCounter = 2
        val variableToTypeMap = mapOf<String, String>()
        val stack = Stack<BooleanExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedBooleanExpressionNode>()
        val labelStack = Stack<String>()

        val fLabel = "f"
        val l = 3
        Mockito.`when`(labelGenerator.generateLabel(labelCounter)).thenReturn(Pair(fLabel, l))

        val leftExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(leftExpression)

        val (ll, t) = binaryOrOperatorExpressionTranslator.translate(
            node, location, trueLabel, falseLabel, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, labelStack
        )

        Assertions.assertEquals(l, ll)
        Assertions.assertEquals(tempCounter, t)
        val topLabel = labelStack.pop()
        Assertions.assertEquals(fLabel, topLabel)
        Mockito.verify(booleanExpressionTranslatorStackPusher).push(LocationConstants.LOCATION_2, node, trueLabel, falseLabel, stack)
        Mockito.verify(booleanExpressionTranslatorStackPusher).push(LocationConstants.LOCATION_1, leftExpression, trueLabel, fLabel, stack)
    }

    @Test
    fun location2Test() {
        val node = Mockito.mock(ParsedBinaryOrOperatorExpressionNode::class.java)
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

        val (l, t) = binaryOrOperatorExpressionTranslator.translate(
            node, location, trueLabel, falseLabel, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, labelStack
        )

        Assertions.assertEquals(labelCounter, l)
        Assertions.assertEquals(tempCounter, t)
        Mockito.verify(booleanExpressionTranslatorStackPusher).push(LocationConstants.LOCATION_3, node, trueLabel, falseLabel, stack)
        Mockito.verify(booleanExpressionTranslatorStackPusher).push(LocationConstants.LOCATION_1, rightExpression, trueLabel, falseLabel, stack)
    }

    @Test
    fun location3Test() {
        val node = Mockito.mock(ParsedBinaryOrOperatorExpressionNode::class.java)
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

        val fLabel = "fLabel"
        labelStack.push(fLabel)

        val labelCode = "labelCode"
        Mockito.`when`(labelCodeGenerator.generateLabelCode(fLabel)).thenReturn(labelCode)

        val leftCode = "leftCode"
        Mockito.`when`(leftExpression.code).thenReturn(listOf(leftCode))

        val rightCode = "rightCode"
        Mockito.`when`(rightExpression.code).thenReturn(listOf(rightCode))

        val (l, t) = binaryOrOperatorExpressionTranslator.translate(
            node, location, trueLabel, falseLabel, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, labelStack
        )

        Assertions.assertEquals(labelCounter, l)
        Assertions.assertEquals(tempCounter, t)
        val top = resultStack.pop()
        Assertions.assertEquals(listOf(leftCode, labelCode, rightCode), top.code)
    }

    @Test
    fun location0Test() {
        val node = Mockito.mock(ParsedBinaryOrOperatorExpressionNode::class.java)
        val location = 0
        val trueLabel = "true"
        val falseLabel = "false"
        val tempCounter = 1
        val labelCounter = 2
        val variableToTypeMap = mapOf<String, String>()
        val stack = Stack<BooleanExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedBooleanExpressionNode>()
        val labelStack = Stack<String>()

        val (l, t) = binaryOrOperatorExpressionTranslator.translate(
            node, location, trueLabel, falseLabel, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, labelStack
        )

        Assertions.assertEquals(labelCounter, l)
        Assertions.assertEquals(tempCounter, t)
    }
}