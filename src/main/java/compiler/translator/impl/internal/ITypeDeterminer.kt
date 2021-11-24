package compiler.translator.impl.internal

internal interface ITypeDeterminer {
    fun determineType(leftType: String, rightType: String): String
}