package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ExpressionTranslatorStackPusherTest {

    private val expressionTranslatorStackPusher = ExpressionTranslatorStackPusher()

    @Test
    fun pushTest() {
        val location = ExpressionTranslatorLocation.MIDDLE
        val expression1 = Mockito.mock(IParsedExpressionNode::class.java)
        val expression2 = Mockito.mock(IParsedExpressionNode::class.java)
        val stack = Stack<ExpressionTranslatorStackItem>()

        expressionTranslatorStackPusher.push(
            location,
            expression1,
            expression2,
            stack
        )

        val location1 = stack.pop()
        Assertions.assertEquals(ExpressionTranslatorLocation.START, location1.location)
        Assertions.assertEquals(expression2, location1.node)

        val top = stack.pop()
        Assertions.assertEquals(location, top.location)
        Assertions.assertEquals(expression1, top.node)
    }
}