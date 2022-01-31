package compiler.frontend.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.frontend.translator.impl.internal.IAssignCodeGenerator

internal class AssignCodeGenerator: IAssignCodeGenerator {
    override fun generateAssignCode(lValue: String, rValue: String): String {
        return lValue +
                PrinterConstants.SPACE +
                PrinterConstants.EQUALS +
                PrinterConstants.SPACE +
                rValue
    }
}