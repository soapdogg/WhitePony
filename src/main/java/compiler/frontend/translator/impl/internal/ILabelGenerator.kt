package compiler.frontend.translator.impl.internal

internal interface ILabelGenerator {
    fun generateLabel(
        labelCounter: Int
    ): Pair<String, Int>
}