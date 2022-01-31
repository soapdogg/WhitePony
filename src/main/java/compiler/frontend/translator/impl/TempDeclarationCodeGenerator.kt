package compiler.frontend.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.frontend.translator.impl.internal.ITempDeclarationCodeGenerator

internal class TempDeclarationCodeGenerator: ITempDeclarationCodeGenerator {
    override fun generateTempDeclarationCode(
        type: String,
        lValue: String,
        rValue: String,
    ): String {
        return type +
                PrinterConstants.SPACE +
                lValue +
                PrinterConstants.SPACE +
                PrinterConstants.EQUALS +
                PrinterConstants.SPACE +
                rValue
    }
}