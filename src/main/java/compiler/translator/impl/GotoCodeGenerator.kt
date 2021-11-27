package compiler.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.translator.impl.internal.IGotoCodeGenerator

internal class GotoCodeGenerator: IGotoCodeGenerator {
    override fun generateGotoCode(label: String): String {
        return PrinterConstants.GOTO +
                PrinterConstants.SPACE +
                label
    }
}