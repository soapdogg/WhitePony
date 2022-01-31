package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.stack.BooleanExpressionTranslatorStackItem
import compiler.core.stack.Stack
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class BooleanExpressionTranslatorStackPusherTest {
    private val booleanExpressionTranslatorStackPusher = BooleanExpressionTranslatorStackPusher()

    @Test
    fun pushTest() {
        val location = 1
        val node = Mockito.mock(IParsedExpressionNode::class.java)
        val trueLabel = "true"
        val falseLabel = "false"
        val stack = Stack<BooleanExpressionTranslatorStackItem>()

        booleanExpressionTranslatorStackPusher.push(
            location,
            node,
            trueLabel,
            falseLabel,
            stack
        )

        val top = stack.pop()
        Assertions.assertEquals(location, top.location)
        Assertions.assertEquals(node, top.node)
        Assertions.assertEquals(trueLabel, top.trueLabel)
        Assertions.assertEquals(falseLabel, top.falseLabel)
    }
}