package compiler.frontend.printer.impl.internal

internal interface ILabelCodeGenerator {
    fun generateLabelCode(label: String): String
}