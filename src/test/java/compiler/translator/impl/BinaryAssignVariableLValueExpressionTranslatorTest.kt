package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryAssignExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IAssignCodeGenerator
import compiler.translator.impl.internal.IExpressionTranslatorStackPusher
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class BinaryAssignVariableLValueExpressionTranslatorTest {
    private val expressionTranslatorStackPusher = Mockito.mock(IExpressionTranslatorStackPusher::class.java)
    private val assignCodeGenerator = Mockito.mock(IAssignCodeGenerator::class.java)

    private val binaryAssignVariableLValueExpressionTranslator = BinaryAssignVariableLValueExpressionTranslator(
        expressionTranslatorStackPusher,
        assignCodeGenerator
    )

    @Test
    fun location1Test() {
        val node = Mockito.mock(ParsedBinaryAssignExpressionNode::class.java)
        val lValueNode = Mockito.mock(ParsedVariableExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(lValueNode)

        val location = ExpressionTranslatorLocation.START
        val variableToTypeMap = mapOf<String, String>()
        val tempCounter = 23
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val rightExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.rightExpression).thenReturn(rightExpression)

        val actual = binaryAssignVariableLValueExpressionTranslator.translate(
            node,
            location,
            variableToTypeMap,
            tempCounter,
            stack,
            resultStack
        )
        Assertions.assertEquals(tempCounter, actual)

        Mockito.verify(expressionTranslatorStackPusher).push(
            ExpressionTranslatorLocation.END,
            node,
            rightExpression,
            stack
        )
    }

    @Test
    fun location2Test() {
        val node = Mockito.mock(ParsedBinaryAssignExpressionNode::class.java)

        val lValueNode = Mockito.mock(ParsedVariableExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(lValueNode)

        val value = "value"
        Mockito.`when`(lValueNode.value).thenReturn(value)

        val location = ExpressionTranslatorLocation.END

        val type = "type"
        val variableToTypeMap = mapOf(
            value to type
        )
        val tempCounter = 2
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val rightExpression = Mockito.mock(TranslatedExpressionNode::class.java)
        resultStack.push(rightExpression)

        val rightExpressionAddress = "rightExpressionAddress"
        Mockito.`when`(rightExpression.address).thenReturn(rightExpressionAddress)

        val rightExpressionCode = "rightExpressionCode"
        Mockito.`when`(rightExpression.code).thenReturn(listOf(rightExpressionCode))

        val assignCode = "assignCode"
        Mockito.`when`(assignCodeGenerator.generateAssignCode(value,rightExpressionAddress)).thenReturn(assignCode)

        val actual = binaryAssignVariableLValueExpressionTranslator.translate(
            node,
            location,
            variableToTypeMap,
            tempCounter,
            stack,
            resultStack
        )
        Assertions.assertEquals(tempCounter, actual)

        val top = resultStack.pop()
        Assertions.assertEquals(rightExpressionAddress, top.address)
        Assertions.assertEquals(listOf(rightExpressionCode, assignCode), top.code)
        Assertions.assertEquals(type, top.type)
    }
}