package compiler.printer.impl

import compiler.core.constants.PrinterConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GotoCodeGeneratorTest {
    private val gotoCodeGenerator = GotoCodeGenerator()

    @Test
    fun generateGotoCodeTest() {
        val label = "label"
        val expected = PrinterConstants.GOTO + PrinterConstants.SPACE + label
        val actual = gotoCodeGenerator.generateGotoCode(label)
        Assertions.assertEquals(expected, actual)
    }
}