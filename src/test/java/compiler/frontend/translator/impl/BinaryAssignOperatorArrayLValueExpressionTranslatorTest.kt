package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryArrayExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryAssignOperatorExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.frontend.translator.impl.internal.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class BinaryAssignOperatorArrayLValueExpressionTranslatorTest {
    private val expressionTranslatorStackPusher = Mockito.mock(IExpressionTranslatorStackPusher::class.java)
    private val tempGenerator = Mockito.mock(ITempGenerator::class.java)
    private val arrayCodeGenerator = Mockito.mock(IArrayCodeGenerator::class.java)
    private val operationCodeGenerator = Mockito.mock(IOperationCodeGenerator::class.java)
    private val tempDeclarationCodeGenerator = Mockito.mock(ITempDeclarationCodeGenerator::class.java)
    private val assignCodeGenerator = Mockito.mock(IAssignCodeGenerator::class.java)

    private val binaryAssignOperatorArrayLValueExpressionTranslator = BinaryAssignOperatorArrayLValueExpressionTranslator(
        expressionTranslatorStackPusher, tempGenerator, arrayCodeGenerator, tempDeclarationCodeGenerator, operationCodeGenerator, assignCodeGenerator
    )

    @Test
    fun startLocationTest() {
        val node = Mockito.mock(ParsedBinaryAssignOperatorExpressionNode::class.java)
        val lValueNode = Mockito.mock(ParsedBinaryArrayExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(lValueNode)

        val location = ExpressionTranslatorLocation.START
        val variableToTypeMap = mapOf<String, String>()
        val tempCounter = 23
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val rightExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(lValueNode.rightExpression).thenReturn(rightExpression)

        val actual = binaryAssignOperatorArrayLValueExpressionTranslator.translate(
            node, location, variableToTypeMap, tempCounter, stack, resultStack
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
    fun middleLocationTest() {
        val node = Mockito.mock(ParsedBinaryAssignOperatorExpressionNode::class.java)
        val lValueNode = Mockito.mock(ParsedBinaryArrayExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(lValueNode)

        val location = ExpressionTranslatorLocation.MIDDLE
        val variableToTypeMap = mapOf<String, String>()
        val tempCounter = 23
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val rightExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.rightExpression).thenReturn(rightExpression)

        val actual = binaryAssignOperatorArrayLValueExpressionTranslator.translate(
            node, location, variableToTypeMap, tempCounter, stack, resultStack
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
    fun endLocationTest() {
        val node = Mockito.mock(ParsedBinaryAssignOperatorExpressionNode::class.java)
        val lValueNode = Mockito.mock(ParsedBinaryArrayExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(lValueNode)

        val variableNode = Mockito.mock(ParsedVariableExpressionNode::class.java)
        Mockito.`when`(lValueNode.leftExpression).thenReturn(variableNode)

        val value = "value"
        Mockito.`when`(variableNode.value).thenReturn(value)

        val location = ExpressionTranslatorLocation.END

        val type = "type"
        val variableToTypeMap = mapOf(
            value to type
        )
        val tempCounter = 23
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val insideArrayExpression = Mockito.mock(TranslatedExpressionNode::class.java)
        resultStack.push(insideArrayExpression)

        val rightExpression = Mockito.mock(TranslatedExpressionNode::class.java)
        resultStack.push(rightExpression)

        val address = "address"
        val tempAfterAddress = 45
        Mockito.`when`(tempGenerator.generateTempVariable(tempCounter)).thenReturn(Pair(address, tempAfterAddress))

        val insideArrayAddress = "insideArrayAddress"
        Mockito.`when`(insideArrayExpression.address).thenReturn(insideArrayAddress)

        val arrayCode = "arrayCode"
        Mockito.`when`(arrayCodeGenerator.generateArrayCode(value, insideArrayAddress)).thenReturn(arrayCode)

        val tempDeclarationCode = "tempDeclarationCode"
        Mockito.`when`(tempDeclarationCodeGenerator.generateTempDeclarationCode(type, address, arrayCode)).thenReturn(tempDeclarationCode)

        val operator = "operator"
        Mockito.`when`(node.operator).thenReturn(operator)

        val rightExpressionAddress = "rightExpressionAddress"
        Mockito.`when`(rightExpression.address).thenReturn(rightExpressionAddress)

        val operationCode = "operationCode"
        Mockito.`when`(operationCodeGenerator.generateOperationCode(address, operator, rightExpressionAddress)).thenReturn(operationCode)

        val addressAssignCode = "addressAssignCode"
        Mockito.`when`(assignCodeGenerator.generateAssignCode(address, operationCode)).thenReturn(addressAssignCode)

        val arrayAssignCode = "arrayAssignCode"
        Mockito.`when`(assignCodeGenerator.generateAssignCode(arrayCode, address)).thenReturn(arrayAssignCode)

        val insideArrayExpressionCode = "insideArrayExpressionCode"
        Mockito.`when`(insideArrayExpression.code).thenReturn(listOf(insideArrayExpressionCode))

        val rightExpressionCode = "rightExpressionCode"
        Mockito.`when`(rightExpression.code).thenReturn(listOf(rightExpressionCode))

        val actual = binaryAssignOperatorArrayLValueExpressionTranslator.translate(node, location, variableToTypeMap, tempCounter, stack, resultStack)
        Assertions.assertEquals(tempAfterAddress, actual)
        val top = resultStack.pop()
        Assertions.assertEquals(address, top.address)
        Assertions.assertEquals(listOf(insideArrayExpressionCode, tempDeclarationCode, rightExpressionCode, addressAssignCode, arrayAssignCode), top.code)
        Assertions.assertEquals(type, top.type)
    }
}