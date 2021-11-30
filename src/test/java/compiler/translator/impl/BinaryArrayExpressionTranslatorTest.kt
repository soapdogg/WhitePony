package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryArrayExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IArrayCodeGenerator
import compiler.translator.impl.internal.IExpressionTranslatorStackPusher
import compiler.translator.impl.internal.ITempDeclarationCodeGenerator
import compiler.translator.impl.internal.ITempGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class BinaryArrayExpressionTranslatorTest {
    private val expressionTranslatorStackPusher = Mockito.mock(IExpressionTranslatorStackPusher::class.java)
    private val tempGenerator = Mockito.mock(ITempGenerator::class.java)
    private val arrayCodeGenerator = Mockito.mock(IArrayCodeGenerator::class.java)
    private val tempDeclarationCodeGenerator = Mockito.mock(ITempDeclarationCodeGenerator::class.java)

    private val binaryArrayExpressionTranslator = BinaryArrayExpressionTranslator(
        expressionTranslatorStackPusher,
        tempGenerator,
        arrayCodeGenerator,
        tempDeclarationCodeGenerator
    )

    @Test
    fun location1Test() {
        val node = Mockito.mock(ParsedBinaryArrayExpressionNode::class.java)
        val location = ExpressionTranslatorLocation.START
        val tempCounter = 1
        val variableToTypeMap = mapOf<String, String>()
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val expression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.rightExpression).thenReturn(expression)

        val actual = binaryArrayExpressionTranslator.translate(
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
            expression,
            stack
        )
    }

    @Test
    fun location2Test() {
        val node = Mockito.mock(ParsedBinaryArrayExpressionNode::class.java)

        val leftExpression = Mockito.mock(ParsedVariableExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(leftExpression)

        val variableValue = "variableValue"
        Mockito.`when`(leftExpression.value).thenReturn(variableValue)

        val location = ExpressionTranslatorLocation.END
        val tempCounter = 1
        val type = "type"
        val variableToTypeMap = mapOf(
            variableValue to type
        )
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val rightExpression = Mockito.mock(TranslatedExpressionNode::class.java)
        resultStack.push(rightExpression)

        val rightExpressionAddress = "rightExpressionAddress"
        Mockito.`when`(rightExpression.address).thenReturn(rightExpressionAddress)

        val address = "address"
        val t = 2
        Mockito.`when`(tempGenerator.generateTempVariable(tempCounter)).thenReturn(Pair(address, t))

        val arrayCode = "arrayCode"
        Mockito.`when`(arrayCodeGenerator.generateArrayCode(variableValue, rightExpressionAddress)).thenReturn(arrayCode)

        val tempDeclarationCode = "tempDeclarationCode"
        Mockito.`when`(
            tempDeclarationCodeGenerator.generateTempDeclarationCode(
                type,
                address,
                arrayCode
            )
        ).thenReturn(tempDeclarationCode)

        val rightExpressionCode = "rightExpressionCode"
        Mockito.`when`(rightExpression.code).thenReturn(listOf(rightExpressionCode))

        val actual = binaryArrayExpressionTranslator.translate(
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
        Assertions.assertEquals(listOf(rightExpressionCode, tempDeclarationCode), top.code)
        Assertions.assertEquals(type, top.type)
    }
}