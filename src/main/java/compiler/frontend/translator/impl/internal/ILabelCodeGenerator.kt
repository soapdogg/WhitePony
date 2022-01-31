package compiler.frontend.translator.impl.internal

internal interface ILabelCodeGenerator {
    fun generateLabelCode(label: String): String
}