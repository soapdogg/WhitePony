package compiler.frontend.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryArrayExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryPreOperatorExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.frontend.translator.impl.internal.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class UnaryPreOperatorArrayExpressionTranslatorTest {
    private val expressionTranslatorStackPusher = Mockito.mock(IExpressionTranslatorStackPusher::class.java)
    private val tempGenerator = Mockito.mock(ITempGenerator::class.java)
    private val arrayCodeGenerator = Mockito.mock(IArrayCodeGenerator::class.java)
    private val tempDeclarationCodeGenerator = Mockito.mock(ITempDeclarationCodeGenerator::class.java)
    private val operationCodeGenerator = Mockito.mock(IOperationCodeGenerator::class.java)
    private val assignCodeGenerator = Mockito.mock(IAssignCodeGenerator::class.java)

    private val unaryPreOperatorArrayExpressionTranslator = UnaryPreOperatorArrayExpressionTranslator(
        expressionTranslatorStackPusher, tempGenerator, arrayCodeGenerator, tempDeclarationCodeGenerator, operationCodeGenerator, assignCodeGenerator
    )

    @Test
    fun startLocationTest() {
        val node = Mockito.mock(ParsedUnaryPreOperatorExpressionNode::class.java)
        val lValueNode = Mockito.mock(ParsedBinaryArrayExpressionNode::class.java)
        Mockito.`when`(node.expression).thenReturn(lValueNode)

        val location = ExpressionTranslatorLocation.START
        val variableToTypeMap = mapOf<String, String>()
        val tempCounter = 23
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val rightExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(lValueNode.rightExpression).thenReturn(rightExpression)

        val actual = unaryPreOperatorArrayExpressionTranslator.translate(
            node, location, variableToTypeMap, tempCounter, stack, resultStack
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
        val node = Mockito.mock(ParsedUnaryPreOperatorExpressionNode::class.java)
        val lValueNode = Mockito.mock(ParsedBinaryArrayExpressionNode::class.java)
        Mockito.`when`(node.expression).thenReturn(lValueNode)
        val variableNode = Mockito.mock(ParsedVariableExpressionNode::class.java)
        Mockito.`when`(lValueNode.leftExpression).thenReturn(variableNode)
        val variableValue = "variableValue"
        Mockito.`when`(variableNode.value).thenReturn(variableValue)

        val location = ExpressionTranslatorLocation.END
        val type = "type"
        val variableToTypeMap = mapOf(
            variableValue to type
        )
        val tempCounter = 23
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val insideExpression = Mockito.mock(TranslatedExpressionNode::class.java)
        resultStack.push(insideExpression)

        val address = "address"
        val tempAfterAddress = 456
        Mockito.`when`(tempGenerator.generateTempVariable(tempCounter)).thenReturn(Pair(address, tempAfterAddress))


        val insideExpressionAddress = "insideExpressionAddress"
        Mockito.`when`(insideExpression.address).thenReturn(insideExpressionAddress)

        val arrayCode = "arrayCode"
        Mockito.`when`(arrayCodeGenerator.generateArrayCode(variableValue, insideExpressionAddress)).thenReturn(arrayCode)

        val tempDeclarationCode = "tempDeclarationCode"
        Mockito.`when`(tempDeclarationCodeGenerator.generateTempDeclarationCode(type, address, arrayCode)).thenReturn(tempDeclarationCode)

        val operator = "operator"
        Mockito.`when`(node.operator).thenReturn(operator)

        val operationCode = "operationCode"
        Mockito.`when`(operationCodeGenerator.generateOperationCode(address, operator, PrinterConstants.ONE)).thenReturn(operationCode)

        val operationAssignCode = "operationAssignCode"
        Mockito.`when`(assignCodeGenerator.generateAssignCode(address, operationCode)).thenReturn(operationAssignCode)

        val arrayAssignCode = "arrayAssignCode"
        Mockito.`when`(assignCodeGenerator.generateAssignCode(arrayCode, address)).thenReturn(arrayAssignCode)

        val insideExpressionCode = "insideExpressionCode"
        Mockito.`when`(insideExpression.code).thenReturn(listOf(insideExpressionCode))

        val actual = unaryPreOperatorArrayExpressionTranslator.translate(node, location, variableToTypeMap, tempCounter, stack, resultStack)
        Assertions.assertEquals(tempAfterAddress, actual)
        val top = resultStack.pop()
        Assertions.assertEquals(address, top.address)
        Assertions.assertEquals(listOf(insideExpressionCode, tempDeclarationCode, operationAssignCode, arrayAssignCode), top.code)
        Assertions.assertEquals(type, top.type)
    }
}