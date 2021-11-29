package compiler.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.ICodeGenerator

internal class CodeGenerator: ICodeGenerator {
    override fun generateCode(code: List<String>): String {
        return code.joinToString(
            PrinterConstants.SEPARATOR,
        )
    }
}