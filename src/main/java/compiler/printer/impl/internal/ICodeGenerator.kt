package compiler.printer.impl.internal

internal interface ICodeGenerator {
    fun generateCode (
        code: List<String>,
    ): String
}