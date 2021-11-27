package compiler.translator.impl.internal

internal interface IGotoCodeGenerator {
    fun generateGotoCode(
        label: String
    ): String
}