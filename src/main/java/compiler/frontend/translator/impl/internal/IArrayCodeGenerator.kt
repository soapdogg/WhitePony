package compiler.frontend.translator.impl.internal

internal interface IArrayCodeGenerator {
    fun generateArrayCode(
        variableValue: String,
        insideValue: String
    ): String
}