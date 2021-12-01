package compiler.translator.impl

import compiler.core.nodes.parsed.ParsedBinaryArrayExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryPostOperatorExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IExpressionTranslator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class UnaryPostOperatorExpressionTranslatorTest {
    private val unaryPostOperatorVariableExpressionTranslator = Mockito.mock(IExpressionTranslator::class.java)
    private val unaryPostOperatorArrayExpressionTranslator = Mockito.mock(IExpressionTranslator::class.java)

    private val unaryPostOperatorExpressionTranslator = UnaryPostOperatorExpressionTranslator(
        unaryPostOperatorVariableExpressionTranslator,
        unaryPostOperatorArrayExpressionTranslator
    )

    @Test
    fun variableExpressionTest() {
        val node = Mockito.mock(ParsedUnaryPostOperatorExpressionNode::class.java)
        val lValueNode = Mockito.mock(ParsedVariableExpressionNode::class.java)
        Mockito.`when`(node.expression).thenReturn(lValueNode)

        val location = ExpressionTranslatorLocation.START
        val variableToTypeMap = mapOf<String, String>()
        val tempCounter = 23
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val updatedTemp = 34434
        Mockito.`when`(unaryPostOperatorVariableExpressionTranslator.translate(node, location, variableToTypeMap, tempCounter, stack, resultStack)).thenReturn(updatedTemp)

        val actual = unaryPostOperatorExpressionTranslator.translate(node, location, variableToTypeMap, tempCounter, stack, resultStack)
        Assertions.assertEquals(updatedTemp, actual)
    }

    @Test
    fun arrayExpressionTest() {
        val node = Mockito.mock(ParsedUnaryPostOperatorExpressionNode::class.java)
        val lValueNode = Mockito.mock(ParsedBinaryArrayExpressionNode::class.java)
        Mockito.`when`(node.expression).thenReturn(lValueNode)

        val location = ExpressionTranslatorLocation.START
        val variableToTypeMap = mapOf<String, String>()
        val tempCounter = 23
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val updatedTemp = 34434
        Mockito.`when`(unaryPostOperatorArrayExpressionTranslator.translate(node, location, variableToTypeMap, tempCounter, stack, resultStack)).thenReturn(updatedTemp)

        val actual = unaryPostOperatorExpressionTranslator.translate(node, location, variableToTypeMap, tempCounter, stack, resultStack)
        Assertions.assertEquals(updatedTemp, actual)
    }
}