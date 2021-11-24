package compiler.translator.impl

import compiler.translator.impl.internal.ITypeDeterminer

internal class TypeDeterminer: ITypeDeterminer {
    override fun determineType(leftType: String, rightType: String): String {
        return if (leftType == "int" && rightType == "int") "int" else "double"
    }
}