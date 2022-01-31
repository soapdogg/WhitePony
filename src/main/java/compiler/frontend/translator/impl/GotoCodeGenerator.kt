package compiler.frontend.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.frontend.translator.impl.internal.IGotoCodeGenerator

internal class GotoCodeGenerator: IGotoCodeGenerator {
    override fun generateGotoCode(label: String): String {
        return PrinterConstants.GOTO +
                PrinterConstants.SPACE +
                label
    }
}