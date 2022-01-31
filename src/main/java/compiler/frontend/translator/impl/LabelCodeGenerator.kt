package compiler.frontend.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.frontend.translator.impl.internal.ILabelCodeGenerator

internal class LabelCodeGenerator: ILabelCodeGenerator {
    override fun generateLabelCode(label: String): String {
        return label + PrinterConstants.COLON + PrinterConstants.SPACE
    }
}