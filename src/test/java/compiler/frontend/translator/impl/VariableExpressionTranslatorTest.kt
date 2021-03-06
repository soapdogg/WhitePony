package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.frontend.translator.impl.internal.ITempDeclarationCodeGenerator
import compiler.frontend.translator.impl.internal.ITempGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class VariableExpressionTranslatorTest {
    private val tempGenerator = Mockito.mock(ITempGenerator::class.java)
    private val tempDeclarationCodeGenerator = Mockito.mock(ITempDeclarationCodeGenerator::class.java)

    private val variableExpressionTranslator = VariableExpressionTranslator(
        tempGenerator,
        tempDeclarationCodeGenerator
    )

    @Test
    fun translateTest() {
        val node = Mockito.mock(ParsedVariableExpressionNode::class.java)
        val location = ExpressionTranslatorLocation.START
        val variable = "variable"
        Mockito.`when`(node.value).thenReturn(variable)

        val type = "type"
        val variableToTypeMap = mapOf(
            variable to type
        )
        val tempCounter = 3
        val stack = Stack<ExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedExpressionNode>()

        val address = "address"
        val t = 4
        Mockito.`when`(tempGenerator.generateTempVariable(tempCounter)).thenReturn(Pair(address, t))

        val tempDeclarationCode = "tempDeclarationCode"
        Mockito.`when`(
            tempDeclarationCodeGenerator.generateTempDeclarationCode(
                type,
                address,
                variable
            )
        ).thenReturn(tempDeclarationCode)

        val actual = variableExpressionTranslator.translate(
            node,
            location,
            variableToTypeMap,
            tempCounter,
            stack,
            resultStack
        )

        Assertions.assertEquals(t, actual)
        val top = resultStack.pop()
        Assertions.assertEquals(address, top.address)
        Assertions.assertEquals(listOf(tempDeclarationCode), top.code)
        Assertions.assertEquals(type, top.type)
    }
}