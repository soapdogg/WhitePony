package compiler.frontend.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.frontend.printer.impl.internal.IGotoCodeGenerator

internal class GotoCodeGenerator: IGotoCodeGenerator {
    override fun generateGotoCode(label: String): String {
        return PrinterConstants.GOTO +
                PrinterConstants.SPACE +
                label
    }
}