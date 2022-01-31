package compiler.frontend.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.frontend.translator.impl.internal.IArrayCodeGenerator

internal class ArrayCodeGenerator: IArrayCodeGenerator {
    override fun generateArrayCode(
        variableValue: String,
        insideValue: String
    ): String {
        return variableValue +
                PrinterConstants.LEFT_BRACKET +
                insideValue +
                PrinterConstants.RIGHT_BRACKET
    }
}