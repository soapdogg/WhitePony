package compiler.frontend.translator.impl

import compiler.core.constants.PrinterConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TempDeclarationCodeGeneratorTest {
    private val tempDeclarationCodeGenerator = TempDeclarationCodeGenerator()

    @Test
    fun generateTempDeclarationCodeTest() {
        val type = "type"
        val lValue = "lValue"
        val rValue = "rValue"

        val expected = type +
                PrinterConstants.SPACE +
                lValue +
                PrinterConstants.SPACE +
                PrinterConstants.EQUALS +
                PrinterConstants.SPACE +
                rValue
        val actual = tempDeclarationCodeGenerator.generateTempDeclarationCode(
            type,
            lValue,
            rValue
        )

        Assertions.assertEquals(expected, actual)
    }
}