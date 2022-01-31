package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryNotOperatorExpressionNode
import compiler.core.nodes.translated.TranslatedBooleanExpressionNode
import compiler.core.stack.BooleanExpressionTranslatorStackItem
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import compiler.frontend.translator.impl.internal.IBooleanExpressionTranslatorStackPusher
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class UnaryNotExpressionTranslatorTest {
    private val booleanExpressionTranslatorStackPusher = Mockito.mock(IBooleanExpressionTranslatorStackPusher::class.java)

    private val unaryNotExpressionTranslator = UnaryNotExpressionTranslator(booleanExpressionTranslatorStackPusher)

    @Test
    fun translateTest() {
        val node = Mockito.mock(ParsedUnaryNotOperatorExpressionNode::class.java)
        val location = LocationConstants.LOCATION_1
        val trueLabel = "true"
        val falseLabel = "false"
        val tempCounter = 1
        val labelCounter = 2
        val variableToTypeMap = mapOf<String, String>()
        val stack = Stack<BooleanExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedBooleanExpressionNode>()
        val labelStack = Stack<String>()

        val expression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.expression).thenReturn(expression)

        val (l, t) = unaryNotExpressionTranslator.translate(
            node, location, trueLabel, falseLabel, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, labelStack
        )
        Assertions.assertEquals(labelCounter, l)
        Assertions.assertEquals(tempCounter, t)
        Mockito.verify(booleanExpressionTranslatorStackPusher).push(
            LocationConstants.LOCATION_1,
            expression,
            falseLabel,
            trueLabel,
            stack
        )
    }
}