package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryArrayExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryAssignExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.frontend.translator.impl.internal.IArrayCodeGenerator
import compiler.frontend.translator.impl.internal.IAssignCodeGenerator
import compiler.frontend.translator.impl.internal.IExpressionTranslatorStackPusher
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class BinaryAssignArrayLValueExpressionTranslatorTest {
    private val expressionTranslatorStackPusher = Mockito.mock(IExpressionTranslatorStackPusher::class.java)
    private val arrayCodeGenerator = Mockito.mock(IArrayCodeGenerator::class.java)
    private val assignCodeGenerator = Mockito.mock(IAssignCodeGenerator::class.java)

    private val binaryAssignArrayLValueExpressionTranslator = BinaryAssignArrayLValueExpressionTranslator(
        expressionTranslatorStackPusher,
        arrayCodeGenerator,
        assignCodeGenerator
    )

    @Test
    fun location1Test() {
        val node = Mockito.mock(ParsedBinaryAssignExpressionNode::class.java)
        val lValueNode = Mockito.mock(ParsedBinaryArrayExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(lValueNode)
        val location = ExpressionTranslatorLocation.START
        val variableToTypeMap = mapOf<String, String>()
        val tempCounter = 45
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val rightExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(lValueNode.rightExpression).thenReturn(rightExpression)

        val actual = binaryAssignArrayLValueExpressionTranslator.translate(
            node,
            location,
            variableToTypeMap,
            tempCounter,
            stack,
            resultStack
        )
        Assertions.assertEquals(tempCounter, actual)

        Mockito.verify(expressionTranslatorStackPusher).push(
            ExpressionTranslatorLocation.MIDDLE,
            node,
            rightExpression,
            stack
        )
    }

    @Test
    fun location2Test() {
        val node = Mockito.mock(ParsedBinaryAssignExpressionNode::class.java)
        val lValueNode = Mockito.mock(ParsedBinaryArrayExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(lValueNode)
        val location = ExpressionTranslatorLocation.MIDDLE
        val variableToTypeMap = mapOf<String, String>()
        val tempCounter = 56
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val rightExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.rightExpression).thenReturn(rightExpression)

        val actual = binaryAssignArrayLValueExpressionTranslator.translate(
            node,
            location,
            variableToTypeMap,
            tempCounter,
            stack,
            resultStack
        )
        Assertions.assertEquals(tempCounter, actual)

        Mockito.verify(expressionTranslatorStackPusher).push(
            ExpressionTranslatorLocation.END,
            node,
            rightExpression,
            stack
        )
    }

    @Test
    fun location3Test() {
        val node = Mockito.mock(ParsedBinaryAssignExpressionNode::class.java)
        val lValueNode = Mockito.mock(ParsedBinaryArrayExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(lValueNode)

        val variableNode = Mockito.mock(ParsedVariableExpressionNode::class.java)
        Mockito.`when`(lValueNode.leftExpression).thenReturn(variableNode)

        val variableValue = "variableValue"
        Mockito.`when`(variableNode.value).thenReturn(variableValue)

        val location = ExpressionTranslatorLocation.END

        val type = "type"
        val variableToTypeMap = mapOf(
            variableValue to type
        )
        val tempCounter = 6
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val insideArrayExpression = Mockito.mock(TranslatedExpressionNode::class.java)
        resultStack.push(insideArrayExpression)

        val rightExpression = Mockito.mock(TranslatedExpressionNode::class.java)
        resultStack.push(rightExpression)


        val insideArrayAddress = "insideArrayAddress"
        Mockito.`when`(insideArrayExpression.address).thenReturn(insideArrayAddress)

        val arrayCode = "arrayCode"
        Mockito.`when`(arrayCodeGenerator.generateArrayCode(variableValue,insideArrayAddress)).thenReturn(arrayCode)

        val rightExpressionAddress = "rightExpressionAddress"
        Mockito.`when`(rightExpression.address).thenReturn(rightExpressionAddress)

        val assignCode = "assignCode"
        Mockito.`when`(assignCodeGenerator.generateAssignCode(arrayCode, rightExpressionAddress)).thenReturn(assignCode)

        val insideArrayExpressionCode = "insideArrayExpressionCode"
        Mockito.`when`(insideArrayExpression.code).thenReturn(listOf(insideArrayExpressionCode))

        val rightExpressionCode = "rightExpressionCode"
        Mockito.`when`(rightExpression.code).thenReturn(listOf(rightExpressionCode))

        val actual = binaryAssignArrayLValueExpressionTranslator.translate(
            node,
            location,
            variableToTypeMap,
            tempCounter,
            stack,
            resultStack
        )
        Assertions.assertEquals(tempCounter, actual)

        val top = resultStack.pop()
        Assertions.assertEquals(rightExpressionAddress, top.address)
        Assertions.assertEquals(listOf(insideArrayExpressionCode, rightExpressionCode, assignCode), top.code)
        Assertions.assertEquals(type, top.type)
    }
}