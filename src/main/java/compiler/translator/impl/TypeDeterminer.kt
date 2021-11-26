package compiler.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.translator.impl.internal.ITypeDeterminer

internal class TypeDeterminer: ITypeDeterminer {
    override fun determineType(leftType: String, rightType: String): String {
        return if (leftType == PrinterConstants.INT && rightType == PrinterConstants.INT) PrinterConstants.INT else PrinterConstants.DOUBLE
    }
}