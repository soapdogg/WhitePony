package compiler.frontend.translator.impl

import compiler.core.constants.PrinterConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AssignCodeGeneratorTest {
    private val assignCodeGenerator = AssignCodeGenerator()

    @Test
    fun generateAssignCodeTest() {
        val lValue = "lValue"
        val rValue = "rValue"

        val expected = lValue +
                PrinterConstants.SPACE +
                PrinterConstants.EQUALS +
                PrinterConstants.SPACE +
                rValue
        val actual = assignCodeGenerator.generateAssignCode(lValue, rValue)
        Assertions.assertEquals(expected, actual)
    }
}