package compiler.frontend.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.parsed.ParsedUnaryPreOperatorExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.frontend.translator.impl.internal.IAssignCodeGenerator
import compiler.frontend.translator.impl.internal.IOperationCodeGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class UnaryPreOperatorVariableExpressionTranslatorTest {
    private val operationCodeGenerator = Mockito.mock(IOperationCodeGenerator::class.java)
    private val assignCodeGenerator = Mockito.mock(IAssignCodeGenerator::class.java)

    private val unaryPreOperatorVariableExpressionTranslator = UnaryPreOperatorVariableExpressionTranslator(
        operationCodeGenerator,
        assignCodeGenerator
    )

    @Test
    fun translateTest() {
        val node = Mockito.mock(ParsedUnaryPreOperatorExpressionNode::class.java)
        val lValueNode = Mockito.mock(ParsedVariableExpressionNode::class.java)
        Mockito.`when`(node.expression).thenReturn(lValueNode)
        val variableValue = "value"
        Mockito.`when`(lValueNode.value).thenReturn(variableValue)

        val location = ExpressionTranslatorLocation.START
        val type = "type"
        val variableToTypeMap = mapOf(
            variableValue to type
        )
        val tempCounter = 23
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val operator = "operator"
        Mockito.`when`(node.operator).thenReturn(operator)

        val operationCode = "operationCode"
        Mockito.`when`(operationCodeGenerator.generateOperationCode(variableValue, operator, PrinterConstants.ONE)).thenReturn(operationCode)

        val assignCode = "assignCode"
        Mockito.`when`(assignCodeGenerator.generateAssignCode(variableValue, operationCode)).thenReturn(assignCode)

        val actual = unaryPreOperatorVariableExpressionTranslator.translate(node, location, variableToTypeMap, tempCounter, stack, resultStack)
        Assertions.assertEquals(tempCounter, actual)
        val top = resultStack.pop()
        Assertions.assertEquals(variableValue, top.address)
        Assertions.assertEquals(listOf(assignCode), top.code)
        Assertions.assertEquals(type, top.type)
    }
}