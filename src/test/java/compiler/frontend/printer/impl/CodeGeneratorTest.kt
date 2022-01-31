package compiler.frontend.printer.impl

import compiler.core.constants.PrinterConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CodeGeneratorTest {
    private val codeGenerator = CodeGenerator()

    @Test
    fun generateCodeTest() {
        val code = listOf("code1", "code2")
        val expected = code.joinToString(
            PrinterConstants.SEPARATOR,
        )
        val actual = codeGenerator.generateCode(code)
        Assertions.assertEquals(expected, actual)
    }
}