package compiler.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.ITabsGenerator

internal class TabsGenerator: ITabsGenerator {
    override fun generateTabs(numberOfTabs: Int): String {
        var tabs = PrinterConstants.EMPTY
        for (i in 0 until numberOfTabs) {
            tabs += PrinterConstants.TAB
        }
        return tabs
    }
}