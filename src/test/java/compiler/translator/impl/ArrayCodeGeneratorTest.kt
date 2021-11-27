package compiler.translator.impl

import compiler.core.constants.PrinterConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ArrayCodeGeneratorTest {
    private val arrayCodeGenerator = ArrayCodeGenerator()

    @Test
    fun generateArrayCodeTest() {
        val variableValue = "variableValue"
        val insideValue = "insideValue"

        val expected = variableValue +
                PrinterConstants.LEFT_BRACKET +
                insideValue +
                PrinterConstants.RIGHT_BRACKET
        val actual = arrayCodeGenerator.generateArrayCode(variableValue, insideValue)
        Assertions.assertEquals(expected, actual)
    }
}