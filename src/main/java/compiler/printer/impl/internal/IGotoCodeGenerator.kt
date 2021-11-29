package compiler.printer.impl.internal

internal interface IGotoCodeGenerator {
    fun generateGotoCode(label: String): String
}