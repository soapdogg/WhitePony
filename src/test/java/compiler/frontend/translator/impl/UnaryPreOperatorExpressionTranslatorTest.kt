package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.*
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.frontend.translator.impl.internal.IExpressionTranslator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class UnaryPreOperatorExpressionTranslatorTest {
    private val unaryPreOperatorVariableExpressionTranslator = Mockito.mock(IExpressionTranslator::class.java)
    private val unaryPreOperatorArrayExpressionTranslator = Mockito.mock(IExpressionTranslator::class.java)

    private val unaryPreOperatorExpressionTranslator = UnaryPreOperatorExpressionTranslator(
        unaryPreOperatorVariableExpressionTranslator,
        unaryPreOperatorArrayExpressionTranslator
    )

    @Test
    fun variableExpressionTest() {
        val node = Mockito.mock(ParsedUnaryPreOperatorExpressionNode::class.java)
        val lValueNode = Mockito.mock(ParsedVariableExpressionNode::class.java)
        Mockito.`when`(node.expression).thenReturn(lValueNode)

        val location = ExpressionTranslatorLocation.START
        val variableToTypeMap = mapOf<String, String>()
        val tempCounter = 23
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val updatedTemp = 34434
        Mockito.`when`(unaryPreOperatorVariableExpressionTranslator.translate(node, location, variableToTypeMap, tempCounter, stack, resultStack)).thenReturn(updatedTemp)

        val actual = unaryPreOperatorExpressionTranslator.translate(node, location, variableToTypeMap, tempCounter, stack, resultStack)
        Assertions.assertEquals(updatedTemp, actual)
    }

    @Test
    fun arrayExpressionTest() {
        val node = Mockito.mock(ParsedUnaryPreOperatorExpressionNode::class.java)
        val lValueNode = Mockito.mock(ParsedBinaryArrayExpressionNode::class.java)
        Mockito.`when`(node.expression).thenReturn(lValueNode)

        val location = ExpressionTranslatorLocation.START
        val variableToTypeMap = mapOf<String, String>()
        val tempCounter = 23
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val updatedTemp = 34434
        Mockito.`when`(unaryPreOperatorArrayExpressionTranslator.translate(node, location, variableToTypeMap, tempCounter, stack, resultStack)).thenReturn(updatedTemp)

        val actual = unaryPreOperatorExpressionTranslator.translate(node, location, variableToTypeMap, tempCounter, stack, resultStack)
        Assertions.assertEquals(updatedTemp, actual)
    }
}