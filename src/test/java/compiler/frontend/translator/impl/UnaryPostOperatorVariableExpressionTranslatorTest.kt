package compiler.frontend.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.parsed.ParsedUnaryPostOperatorExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.frontend.translator.impl.internal.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class UnaryPostOperatorVariableExpressionTranslatorTest {
    private val tempGenerator = Mockito.mock(ITempGenerator::class.java)
    private val tempDeclarationCodeGenerator = Mockito.mock(ITempDeclarationCodeGenerator::class.java)
    private val operationCodeGenerator = Mockito.mock(IOperationCodeGenerator::class.java)
    private val assignCodeGenerator = Mockito.mock(IAssignCodeGenerator::class.java)

    private val unaryPostOperatorVariableExpressionTranslator = UnaryPostOperatorVariableExpressionTranslator(
        tempGenerator, tempDeclarationCodeGenerator, operationCodeGenerator, assignCodeGenerator
    )

    @Test
    fun translateTest() {
        val node = Mockito.mock(ParsedUnaryPostOperatorExpressionNode::class.java)
        val lValueNode = Mockito.mock(ParsedVariableExpressionNode::class.java)
        Mockito.`when`(node.expression).thenReturn(lValueNode)
        val variableValue = "variableValue"
        Mockito.`when`(lValueNode.value).thenReturn(variableValue)

        val location = ExpressionTranslatorLocation.START
        val type = "type"
        val variableToTypeMap = mapOf(
            variableValue to type
        )
        val tempCounter = 23
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val address = "address"
        val tempAfterAddress = 45
        Mockito.`when`(tempGenerator.generateTempVariable(tempCounter)).thenReturn(Pair(address, tempAfterAddress))

        val tempDeclarationCode = "tempDeclarationCode"
        Mockito.`when`(tempDeclarationCodeGenerator.generateTempDeclarationCode(type, address, variableValue)).thenReturn(tempDeclarationCode)

        val operator = "operator"
        Mockito.`when`(node.operator).thenReturn(operator)

        val operationCode = "operationCode"
        Mockito.`when`(operationCodeGenerator.generateOperationCode(variableValue, operator, PrinterConstants.ONE)).thenReturn(operationCode)

        val assignCode = "assignCode"
        Mockito.`when`(assignCodeGenerator.generateAssignCode(variableValue, operationCode)).thenReturn(assignCode)

        val actual = unaryPostOperatorVariableExpressionTranslator.translate(node, location, variableToTypeMap, tempCounter, stack, resultStack)
        Assertions.assertEquals(tempAfterAddress, actual)
        val top = resultStack.pop()
        Assertions.assertEquals(address, top.address)
        Assertions.assertEquals(listOf(tempDeclarationCode, assignCode), top.code)
        Assertions.assertEquals(type, top.type)
    }
}