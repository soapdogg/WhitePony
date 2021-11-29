package compiler.printer.impl

import compiler.core.constants.PrinterConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LabelCodeGeneratorTest {
    private val labelCodeGenerator = LabelCodeGenerator()

    @Test
    fun generateLabelCodeTest() {
        val label = "label"
        val expected = label + PrinterConstants.COLON + PrinterConstants.SPACE
        val actual = labelCodeGenerator.generateLabelCode(label)
        Assertions.assertEquals(expected, actual)
    }
}