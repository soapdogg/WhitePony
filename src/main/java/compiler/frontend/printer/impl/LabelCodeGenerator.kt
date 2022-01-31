package compiler.frontend.printer.impl

import compiler.core.constants.PrinterConstants
import compiler.frontend.printer.impl.internal.ILabelCodeGenerator

internal class LabelCodeGenerator: ILabelCodeGenerator {
    override fun generateLabelCode(label: String): String {
        return label + PrinterConstants.COLON + PrinterConstants.SPACE
    }
}