package compiler.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.IGotoCodeGenerator

internal class GotoCodeGenerator: IGotoCodeGenerator {
    override fun generateGotoCode(label: String): String {
        return PrinterConstants.GOTO +
                PrinterConstants.SPACE +
                label
    }
}