package compiler.frontend.printer.impl.internal

internal interface IGotoCodeGenerator {
    fun generateGotoCode(label: String): String
}