package compiler.translator.impl

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class StackGeneratorTest {
    private val stackGenerator = StackGenerator()

    @Test
    fun generateStackTest() {
        val stack = stackGenerator.generateNewStack(String::class.java)

        Assertions.assertNotNull(stack)
    }
}