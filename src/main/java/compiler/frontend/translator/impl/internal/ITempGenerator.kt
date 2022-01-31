package compiler.frontend.translator.impl.internal

internal interface ITempGenerator {
    fun generateTempVariable(
        tempCounter: Int
    ): Pair<String, Int>
}