package compiler.translator.impl.internal

internal interface ITempGenerator {
    fun generateTempVariable(
        tempCounter: Int,
        variableToTypeMap: Map<String, String>
    ): Pair<String, Int>
}