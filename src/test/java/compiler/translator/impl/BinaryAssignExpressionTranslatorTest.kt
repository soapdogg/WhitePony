package compiler.translator.impl

import compiler.core.nodes.parsed.ParsedBinaryArrayExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryAssignExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IExpressionTranslator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class BinaryAssignExpressionTranslatorTest {
    private val binaryAssignVariableLValueExpressionTranslator = Mockito.mock(IExpressionTranslator::class.java)
    private val binaryAssignArrayLValueExpressionTranslator = Mockito.mock(IExpressionTranslator::class.java)

    private val binaryAssignExpressionTranslator = BinaryAssignExpressionTranslator(
        binaryAssignVariableLValueExpressionTranslator,
        binaryAssignArrayLValueExpressionTranslator
    )

    @Test
    fun leftExpressionVariableTest() {
        val node = Mockito.mock(ParsedBinaryAssignExpressionNode::class.java)
        val location = ExpressionTranslatorLocation.START
        val tempCounter = 3
        val variableToTypeMap = mapOf<String,String>()
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val leftExpression = Mockito.mock(ParsedVariableExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(leftExpression)
        Mockito.`when`(binaryAssignVariableLValueExpressionTranslator.translate(
            node,
            location,
            variableToTypeMap,
            tempCounter,
            stack,
            resultStack
        )).thenReturn(tempCounter)
        val actual = binaryAssignExpressionTranslator.translate(
            node,
            location,
            variableToTypeMap,
            tempCounter,
            stack,
            resultStack
        )
        Assertions.assertEquals(tempCounter, actual)
    }

    @Test
    fun leftExpressionArrayTest() {
        val node = Mockito.mock(ParsedBinaryAssignExpressionNode::class.java)
        val location = ExpressionTranslatorLocation.START
        val variableToTypeMap = mapOf<String,String>()
        val tempCounter = 0
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val leftExpression = Mockito.mock(ParsedBinaryArrayExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(leftExpression)

        Mockito.`when`(binaryAssignArrayLValueExpressionTranslator.translate(
            node,
            location,
            variableToTypeMap,
            tempCounter,
            stack,
            resultStack
        )).thenReturn(tempCounter)

        val actual = binaryAssignExpressionTranslator.translate(
            node,
            location,
            variableToTypeMap,
            tempCounter,
            stack,
            resultStack
        )
        Assertions.assertEquals(tempCounter, actual)

    }
}