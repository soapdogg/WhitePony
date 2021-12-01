package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryAssignOperatorExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.translator.impl.internal.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class BinaryAssignOperatorVariableLValueExpressionTranslatorTest {
    private val expressionTranslatorStackPusher = Mockito.mock(IExpressionTranslatorStackPusher::class.java)
    private val tempGenerator = Mockito.mock(ITempGenerator::class.java)
    private val operationCodeGenerator = Mockito.mock(IOperationCodeGenerator::class.java)
    private val tempDeclarationCodeGenerator = Mockito.mock(ITempDeclarationCodeGenerator::class.java)
    private val assignCodeGenerator = Mockito.mock(IAssignCodeGenerator::class.java)

    private val binaryAssignOperatorVariableLValueExpressionTranslator = BinaryAssignOperatorVariableLValueExpressionTranslator(
        expressionTranslatorStackPusher, tempGenerator, operationCodeGenerator, tempDeclarationCodeGenerator, assignCodeGenerator
    )

    @Test
    fun startLocationTest() {
        val node = Mockito.mock(ParsedBinaryAssignOperatorExpressionNode::class.java)
        val lValueNode = Mockito.mock(ParsedVariableExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(lValueNode)

        val location = ExpressionTranslatorLocation.START
        val variableToTypeMap = mapOf<String, String>()
        val tempCounter = 23
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val rightExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.rightExpression).thenReturn(rightExpression)

        val actual = binaryAssignOperatorVariableLValueExpressionTranslator.translate(
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
    fun endLocationTest() {
        val node = Mockito.mock(ParsedBinaryAssignOperatorExpressionNode::class.java)
        val lValueNode = Mockito.mock(ParsedVariableExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(lValueNode)

        val value = "value"
        Mockito.`when`(lValueNode.value).thenReturn(value)

        val location = ExpressionTranslatorLocation.END

        val type = "type"
        val variableToTypeMap = mapOf(
            value to type
        )
        val tempCounter = 23
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val rightExpression = Mockito.mock(TranslatedExpressionNode::class.java)
        resultStack.push(rightExpression)

        val address = "address"
        val tempAfterAddress = 4
        Mockito.`when`(tempGenerator.generateTempVariable(tempCounter)).thenReturn(Pair(address, tempAfterAddress))

        val operator = "operator"
        Mockito.`when`(node.operator).thenReturn(operator)

        val rightExpressionAddress = "rightExpressionAddress"
        Mockito.`when`(rightExpression.address).thenReturn(rightExpressionAddress)

        val tempDeclarationRValue = "tempDeclarationRValue"
        Mockito.`when`(operationCodeGenerator.generateOperationCode(value, operator, rightExpressionAddress)).thenReturn(tempDeclarationRValue)

        val tempDeclarationCode = "tempDeclarationCode"
        Mockito.`when`(tempDeclarationCodeGenerator.generateTempDeclarationCode(type, address, tempDeclarationRValue)).thenReturn(tempDeclarationCode)

        val assignCode = "assignCode"
        Mockito.`when`(assignCodeGenerator.generateAssignCode(value, address)).thenReturn(assignCode)

        val rightExpressionCode = "rightExpressionCode"
        Mockito.`when`(rightExpression.code).thenReturn(listOf(rightExpressionCode))

        val actual = binaryAssignOperatorVariableLValueExpressionTranslator.translate(node, location, variableToTypeMap, tempCounter, stack, resultStack)
        Assertions.assertEquals(tempAfterAddress, actual)
        val top = resultStack.pop()
        Assertions.assertEquals(address, top.address)
        Assertions.assertEquals(listOf(rightExpressionCode, tempDeclarationCode, assignCode), top.code)
        Assertions.assertEquals(type, top.type)
    }
}