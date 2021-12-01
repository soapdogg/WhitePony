package compiler.translator.impl

import compiler.core.nodes.parsed.ParsedBinaryArrayExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryAssignOperatorExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IExpressionTranslator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class BinaryAssignOperatorExpressionTranslatorTest {
    private val binaryAssignOperatorVariableLValueExpressionTranslator = Mockito.mock(IExpressionTranslator::class.java)
    private val binaryAssignOperatorArrayLValueExpressionTranslator = Mockito.mock(IExpressionTranslator::class.java)

    private val binaryAssignOperatorExpressionTranslator = BinaryAssignOperatorExpressionTranslator(
        binaryAssignOperatorVariableLValueExpressionTranslator, binaryAssignOperatorArrayLValueExpressionTranslator
    )

    @Test
    fun variableLValueTest() {
        val node = Mockito.mock(ParsedBinaryAssignOperatorExpressionNode::class.java)
        val lValueNode = Mockito.mock(ParsedVariableExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(lValueNode)

        val location = ExpressionTranslatorLocation.START
        val variableToTypeMap = mapOf<String, String>()
        val tempCounter = 23
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val updatedTemp = 3
        Mockito.`when`(
            binaryAssignOperatorVariableLValueExpressionTranslator.translate(
                node, location, variableToTypeMap, tempCounter, stack, resultStack
            )
        ).thenReturn(updatedTemp)

        val actual = binaryAssignOperatorExpressionTranslator.translate(node, location, variableToTypeMap, tempCounter, stack, resultStack)
        Assertions.assertEquals(updatedTemp, actual)
    }

    @Test
    fun arrayLValueTest() {
        val node = Mockito.mock(ParsedBinaryAssignOperatorExpressionNode::class.java)
        val lValueNode = Mockito.mock(ParsedBinaryArrayExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(lValueNode)

        val location = ExpressionTranslatorLocation.START
        val variableToTypeMap = mapOf<String, String>()
        val tempCounter = 23
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val updatedTemp = 333
        Mockito.`when`(
            binaryAssignOperatorArrayLValueExpressionTranslator.translate(
                node, location, variableToTypeMap, tempCounter, stack, resultStack
            )
        ).thenReturn(updatedTemp)

        val actual = binaryAssignOperatorExpressionTranslator.translate(node, location, variableToTypeMap, tempCounter, stack, resultStack)
        Assertions.assertEquals(updatedTemp, actual)
    }
}