package compiler.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.printer.impl.internal.ILabelCodeGenerator

internal class LabelCodeGenerator: ILabelCodeGenerator {
    override fun generateLabelCode(label: String): String {
        return label + PrinterConstants.COLON + PrinterConstants.SPACE
    }
}