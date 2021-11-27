package compiler.translator.impl

import compiler.core.constants.TokenizerConstants
import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IExpressionTranslatorStackPusher
import compiler.translator.impl.internal.ITempDeclarationCodeGenerator
import compiler.translator.impl.internal.ITempGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class UnaryExpressionTranslatorTest {
    private val expressionTranslatorStackPusher = Mockito.mock(IExpressionTranslatorStackPusher::class.java)
    private val tempGenerator = Mockito.mock(ITempGenerator::class.java)
    private val tempDeclarationCodeGenerator = Mockito.mock(ITempDeclarationCodeGenerator::class.java)

    private val unaryExpressionTranslator = UnaryExpressionTranslator(
        expressionTranslatorStackPusher,
        tempGenerator,
        tempDeclarationCodeGenerator
    )

    @Test
    fun location1Test() {
        val node = Mockito.mock(ParsedUnaryExpressionNode::class.java)
        val location = LocationConstants.LOCATION_1
        val tempCounter = 1
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val expression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.expression).thenReturn(expression)

        val actual = unaryExpressionTranslator.translate(
            node,
            location,
            tempCounter,
            stack,
            resultStack
        )
        Assertions.assertEquals(tempCounter, actual)
        Mockito.verify(expressionTranslatorStackPusher).push(
            LocationConstants.LOCATION_2,
            node,
            expression,
            stack
        )
    }

    @Test
    fun location2PlusOperatorTest() {
        val node = Mockito.mock(ParsedUnaryExpressionNode::class.java)
        val location = LocationConstants.LOCATION_2
        val tempCounter = 1
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val expression = Mockito.mock(TranslatedExpressionNode::class.java)
        resultStack.push(expression)

        val operator = TokenizerConstants.PLUS_OPERATOR
        Mockito.`when`(node.operator).thenReturn(operator)

        val actual = unaryExpressionTranslator.translate(
            node,
            location,
            tempCounter,
            stack,
            resultStack
        )
        Assertions.assertEquals(tempCounter, actual)
        val top = resultStack.pop()
        Assertions.assertEquals(expression, top)
    }

    @Test
    fun location2NotPlusOperatorTest() {
        val node = Mockito.mock(ParsedUnaryExpressionNode::class.java)
        val location = LocationConstants.LOCATION_2
        val tempCounter = 1
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val expression = Mockito.mock(TranslatedExpressionNode::class.java)
        resultStack.push(expression)

        val operator = TokenizerConstants.MINUS_OPERATOR
        Mockito.`when`(node.operator).thenReturn(operator)

        val address = "address"
        val t = 2
        Mockito.`when`(tempGenerator.generateTempVariable(tempCounter)).thenReturn(Pair(address, t))

        val expressionAddress = "expressionAddress"
        Mockito.`when`(expression.address).thenReturn(expressionAddress)
        val rValue = operator + expressionAddress

        val type = "type"
        Mockito.`when`(expression.type).thenReturn(type)

        val tempDeclarationCode = "tempDeclarationCode"
        Mockito.`when`(
            tempDeclarationCodeGenerator.generateTempDeclarationCode(
                type,
                address,
                rValue
            )
        ).thenReturn(tempDeclarationCode)

        val expressionCode = "expressionCode"
        Mockito.`when`(expression.code).thenReturn(listOf(expressionCode))

        val actual = unaryExpressionTranslator.translate(
            node,
            location,
            tempCounter,
            stack,
            resultStack
        )

        Assertions.assertEquals(t, actual)

        val top = resultStack.pop()
        Assertions.assertEquals(address, top.address)
        Assertions.assertEquals(listOf(expressionCode, tempDeclarationCode), top.code)
        Assertions.assertEquals(type, top.type)
    }

    @Test
    fun location3Test() {
        val node = Mockito.mock(ParsedUnaryExpressionNode::class.java)
        val location = LocationConstants.LOCATION_3
        val tempCounter = 1
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val actual = unaryExpressionTranslator.translate(
            node,
            location,
            tempCounter,
            stack,
            resultStack
        )
        Assertions.assertEquals(tempCounter, actual)
    }
}