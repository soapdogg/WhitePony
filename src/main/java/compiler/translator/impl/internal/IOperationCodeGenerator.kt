package compiler.translator.impl.internal

internal interface IOperationCodeGenerator {
    fun generateOperationCode(
        lValue: String,
        operator: String,
        rValue: String
    ): String
}