package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedInnerExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class InnerExpressionTranslatorTest {
    private val innerExpressionTranslator = InnerExpressionTranslator()

    @Test
    fun translateTest() {
        val node = Mockito.mock(ParsedInnerExpressionNode::class.java)
        val stack = Stack<ExpressionTranslatorStackItem>()

        val expression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.expression).thenReturn(expression)

        innerExpressionTranslator.translate(node, stack)

        val top = stack.pop()

        Assertions.assertEquals(ExpressionTranslatorLocation.START, top.location)
        Assertions.assertEquals(expression, top.node)
    }
}