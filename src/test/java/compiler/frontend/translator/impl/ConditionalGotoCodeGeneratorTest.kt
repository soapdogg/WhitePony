package compiler.frontend.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.frontend.translator.impl.internal.IGotoCodeGenerator
import compiler.frontend.translator.impl.internal.IOperationCodeGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ConditionalGotoCodeGeneratorTest {
    private val operationCodeGenerator = Mockito.mock(IOperationCodeGenerator::class.java)
    private val gotoCodeGenerator = Mockito.mock(IGotoCodeGenerator::class.java)

    private val conditionalGotoCodeGenerator = ConditionalGotoCodeGenerator(
        operationCodeGenerator,
        gotoCodeGenerator
    )

    @Test
    fun generateConditionalGotoCodeTest() {
        val leftAddress = "leftAddress"
        val operator = "operator"
        val rightAddress = "rightAddress"
        val trueLabel = "trueLabel"

        val operationCode = "operationCode"
        Mockito.`when`(operationCodeGenerator.generateOperationCode(leftAddress, operator, rightAddress)).thenReturn(operationCode)

        val gotoTrueLabelCode = "gotoTrueLabelCode"
        Mockito.`when`(gotoCodeGenerator.generateGotoCode(trueLabel)).thenReturn(gotoTrueLabelCode)

        val expected = PrinterConstants.IF +
                PrinterConstants.SPACE +
                PrinterConstants.LEFT_PARENTHESES +
                operationCode +
                PrinterConstants.RIGHT_PARENTHESES +
                PrinterConstants.SPACE +
                gotoTrueLabelCode

        val actual = conditionalGotoCodeGenerator.generateConditionalGotoCode(leftAddress, operator, rightAddress, trueLabel)
        Assertions.assertEquals(expected, actual)
    }
}