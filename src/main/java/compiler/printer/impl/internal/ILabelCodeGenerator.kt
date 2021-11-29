package compiler.printer.impl.internal

internal interface ILabelCodeGenerator {
    fun generateLabelCode(label: String): String
}