package compiler.printer.impl

import compiler.core.constants.PrinterConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TabsGeneratorTest {
    private val tabsGenerator = TabsGenerator()

    @Test
    fun generateTabsTest() {
        val tabs = tabsGenerator.generateTabs(1)
        Assertions.assertEquals(PrinterConstants.TAB, tabs)
    }
}