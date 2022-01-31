package compiler.frontend.translator.impl

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TempGeneratorTest {
    private val tempGenerator = TempGenerator()

    @Test
    fun generateTempVariableTest() {
        val tempCounter = 2

        val (address, updatedTempCounter) = tempGenerator.generateTempVariable(tempCounter)
        Assertions.assertEquals("_t$tempCounter", address)
        Assertions.assertEquals(tempCounter + 1, updatedTempCounter)
    }
}