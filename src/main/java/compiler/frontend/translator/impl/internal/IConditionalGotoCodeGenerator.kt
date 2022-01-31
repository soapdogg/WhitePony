package compiler.frontend.translator.impl.internal

internal interface IConditionalGotoCodeGenerator {
    fun generateConditionalGotoCode(
        leftAddress: String,
        operator: String,
        rightAddress: String,
        trueLabel: String,
    ): String
}