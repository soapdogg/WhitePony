package compiler.frontend.translator.impl.internal

internal interface IAssignCodeGenerator {
    fun generateAssignCode(
        lValue: String,
        rValue: String
    ): String
}