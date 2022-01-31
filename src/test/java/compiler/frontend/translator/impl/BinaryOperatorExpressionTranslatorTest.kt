package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryOperatorExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.frontend.translator.impl.internal.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class BinaryOperatorExpressionTranslatorTest {
    private val expressionTranslatorStackPusher = Mockito.mock(IExpressionTranslatorStackPusher::class.java)
    private val tempGenerator = Mockito.mock(ITempGenerator::class.java)
    private val typeDeterminer = Mockito.mock(ITypeDeterminer::class.java)
    private val operationCodeGenerator = Mockito.mock(IOperationCodeGenerator::class.java)
    private val tempDeclarationCodeGenerator = Mockito.mock(ITempDeclarationCodeGenerator::class.java)

    private val binaryOperatorExpressionTranslator = BinaryOperatorExpressionTranslator(
        expressionTranslatorStackPusher,
        tempGenerator,
        typeDeterminer,
        operationCodeGenerator,
        tempDeclarationCodeGenerator
    )

    @Test
    fun location1Test() {
        val node = Mockito.mock(ParsedBinaryOperatorExpressionNode::class.java)
        val location = ExpressionTranslatorLocation.START
        val tempCounter = 1
        val variableToTypeMap = mapOf<String, String>()
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val leftExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(leftExpression)

        val actual = binaryOperatorExpressionTranslator.translate(
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
            leftExpression,
            stack
        )
    }

    @Test
    fun location2Test() {
        val node = Mockito.mock(ParsedBinaryOperatorExpressionNode::class.java)
        val location = ExpressionTranslatorLocation.MIDDLE
        val tempCounter = 1
        val variableToTypeMap = mapOf<String, String>()
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val rightExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.rightExpression).thenReturn(rightExpression)

        val actual = binaryOperatorExpressionTranslator.translate(
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
        val node = Mockito.mock(ParsedBinaryOperatorExpressionNode::class.java)
        val location = ExpressionTranslatorLocation.END
        val tempCounter = 1
        val variableToTypeMap = mapOf<String, String>()
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val leftExpression = Mockito.mock(TranslatedExpressionNode::class.java)
        resultStack.push(leftExpression)

        val rightExpression = Mockito.mock(TranslatedExpressionNode::class.java)
        resultStack.push(rightExpression)

        val address = "address"
        val t = 2
        Mockito.`when`(tempGenerator.generateTempVariable(tempCounter)).thenReturn(Pair(address, t))

        val lType = "lType"
        Mockito.`when`(leftExpression.type).thenReturn(lType)

        val rType = "rType"
        Mockito.`when`(rightExpression.type).thenReturn(rType)

        val type = "type"
        Mockito.`when`(typeDeterminer.determineType(lType, rType)).thenReturn(type)

        val lAddress = "lAddress"
        Mockito.`when`(leftExpression.address).thenReturn(lAddress)

        val rAddress = "rAddress"
        Mockito.`when`(rightExpression.address).thenReturn(rAddress)

        val operator = "operator"
        Mockito.`when`(node.operator).thenReturn(operator)

        val operationCode = "operationCode"
        Mockito.`when`(operationCodeGenerator.generateOperationCode(lAddress, operator, rAddress)).thenReturn(operationCode)

        val tempDeclarationCode = "tempDeclarationCode"
        Mockito.`when`(tempDeclarationCodeGenerator.generateTempDeclarationCode(type, address, operationCode)).thenReturn(tempDeclarationCode)

        val leftCode = "leftCode"
        Mockito.`when`(leftExpression.code).thenReturn(listOf(leftCode))

        val rightCode = "rightCode"
        Mockito.`when`(rightExpression.code).thenReturn(listOf(rightCode))

        val actual = binaryOperatorExpressionTranslator.translate(
            node,
            location,
            variableToTypeMap,
            tempCounter,
            stack,
            resultStack
        )

        Assertions.assertEquals(t, actual)
        val top = resultStack.pop()
        Assertions.assertEquals(address, top.address)
        Assertions.assertEquals(listOf(leftCode, rightCode, tempDeclarationCode), top.code)
        Assertions.assertEquals(type, top.type)
    }
}