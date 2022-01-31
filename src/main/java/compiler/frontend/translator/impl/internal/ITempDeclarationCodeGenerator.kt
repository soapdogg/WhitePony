package compiler.frontend.translator.impl.internal

internal interface ITempDeclarationCodeGenerator {
    fun generateTempDeclarationCode(
        type: String,
        lValue: String,
        rValue: String
    ): String
}