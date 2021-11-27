package compiler.translator.impl

import compiler.core.nodes.parsed.ParsedBinaryArrayExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryAssignExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IBinaryAssignArrayLValueExpressionTranslator
import compiler.translator.impl.internal.IBinaryAssignVariableLValueExpressionTranslator
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class BinaryAssignExpressionTranslatorTest {
    private val binaryAssignVariableLValueExpressionTranslator = Mockito.mock(IBinaryAssignVariableLValueExpressionTranslator::class.java)
    private val binaryAssignArrayLValueExpressionTranslator = Mockito.mock(IBinaryAssignArrayLValueExpressionTranslator::class.java)

    private val binaryAssignExpressionTranslator = BinaryAssignExpressionTranslator(
        binaryAssignVariableLValueExpressionTranslator,
        binaryAssignArrayLValueExpressionTranslator
    )

    @Test
    fun leftExpressionVariableTest() {
        val node = Mockito.mock(ParsedBinaryAssignExpressionNode::class.java)
        val location = LocationConstants.LOCATION_1
        val variableToTypeMap = mapOf<String,String>()
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val leftExpression = Mockito.mock(ParsedVariableExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(leftExpression)

        binaryAssignExpressionTranslator.translate(
            node,
            location,
            variableToTypeMap,
            stack,
            resultStack
        )

        Mockito.verify(binaryAssignVariableLValueExpressionTranslator).translate(
            node,
            leftExpression,
            location,
            variableToTypeMap,
            stack,
            resultStack
        )
    }

    @Test
    fun leftExpressionArrayTest() {
        val node = Mockito.mock(ParsedBinaryAssignExpressionNode::class.java)
        val location = LocationConstants.LOCATION_1
        val variableToTypeMap = mapOf<String,String>()
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val leftExpression = Mockito.mock(ParsedBinaryArrayExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(leftExpression)

        binaryAssignExpressionTranslator.translate(
            node,
            location,
            variableToTypeMap,
            stack,
            resultStack
        )

        Mockito.verify(binaryAssignArrayLValueExpressionTranslator).translate(
            node,
            leftExpression,
            location,
            variableToTypeMap,
            stack,
            resultStack
        )
    }
}