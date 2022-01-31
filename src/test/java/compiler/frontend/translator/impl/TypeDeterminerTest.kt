package compiler.frontend.translator.impl

import compiler.core.constants.PrinterConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TypeDeterminerTest {
    private val typeDeterminer = TypeDeterminer()

    @Test
    fun intTest() {
        val actual = typeDeterminer.determineType(PrinterConstants.INT, PrinterConstants.INT)
        Assertions.assertEquals(PrinterConstants.INT, actual)
    }

    @Test
    fun doubleTest() {
        val actual = typeDeterminer.determineType(PrinterConstants.DOUBLE, PrinterConstants.INT)
        Assertions.assertEquals(PrinterConstants.DOUBLE, actual)
    }
}