package compiler.translator.impl

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LabelGeneratorTest {
    private val labelGenerator = LabelGenerator()

    @Test
    fun generateLabelTest() {
        val labelCounter = 1
        val expected = "_l$labelCounter"
        val (actual, nextLabelCounter) = labelGenerator.generateLabel(labelCounter)
        Assertions.assertEquals(expected, actual)
        Assertions.assertEquals(labelCounter + 1, nextLabelCounter)
    }
}