package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.ParsedConstantExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ConstantExpressionTranslatorTest {
    private val constantExpressionTranslator = ConstantExpressionTranslator()

    @Test
    fun translateTest() {
        val node = Mockito.mock(ParsedConstantExpressionNode::class.java)
        val location = ExpressionTranslatorLocation.START
        val variableToTypeMap = mapOf<String,String>()
        val tempCounter = 1
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val value = "value"
        Mockito.`when`(node.value).thenReturn(value)

        val type = "type"
        Mockito.`when`(node.type).thenReturn(type)

        val actual = constantExpressionTranslator.translate(node, location, variableToTypeMap, tempCounter, stack, resultStack)
        Assertions.assertEquals(tempCounter, actual)

        val top = resultStack.pop()
        Assertions.assertEquals(value, top.address)
        Assertions.assertTrue(top.code.isEmpty())
        Assertions.assertEquals(type, top.type)
    }
}