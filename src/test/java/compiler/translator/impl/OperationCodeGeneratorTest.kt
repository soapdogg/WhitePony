package compiler.translator.impl

import compiler.core.constants.PrinterConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class OperationCodeGeneratorTest {

    private val operationCodeGenerator = OperationCodeGenerator()

    @Test
    fun generateOperationCodeTest() {
        val lValue = "lValue"
        val operator = "operator"
        val rValue = "rValue"

        val expected = lValue +
                PrinterConstants.SPACE +
                operator +
                PrinterConstants.SPACE +
                rValue
        val actual = operationCodeGenerator.generateOperationCode(
            lValue,
            operator,
            rValue
        )
        Assertions.assertEquals(expected, actual)
    }
}