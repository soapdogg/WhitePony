package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedInnerExpressionNode
import compiler.core.nodes.translated.TranslatedBooleanExpressionNode
import compiler.core.stack.BooleanExpressionTranslatorStackItem
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IBooleanExpressionTranslatorStackPusher
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class InnerBooleanExpressionTranslatorTest {
    private val booleanExpressionTranslatorStackPusher = Mockito.mock(IBooleanExpressionTranslatorStackPusher::class.java)

    private val innerBooleanExpressionTranslator = InnerBooleanExpressionTranslator(booleanExpressionTranslatorStackPusher)

    @Test
    fun translateTest() {
        val node = Mockito.mock(ParsedInnerExpressionNode::class.java)
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

        val (l, t) = innerBooleanExpressionTranslator.translate(
            node, location, trueLabel, falseLabel, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, labelStack
        )
        Assertions.assertEquals(labelCounter, l)
        Assertions.assertEquals(tempCounter, t)
        Mockito.verify(booleanExpressionTranslatorStackPusher).push(
            LocationConstants.LOCATION_1,
            expression,
            trueLabel,
            falseLabel,
            stack
        )
    }
}