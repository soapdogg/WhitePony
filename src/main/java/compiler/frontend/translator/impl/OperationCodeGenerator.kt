package compiler.frontend.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.frontend.translator.impl.internal.IOperationCodeGenerator

internal class OperationCodeGenerator: IOperationCodeGenerator {
    override fun generateOperationCode(
        lValue: String,
        operator: String,
        rValue: String
    ): String {
        return lValue +
            PrinterConstants.SPACE +
            operator +
            PrinterConstants.SPACE +
            rValue
    }
}