package compiler.translator.impl

import compiler.translator.impl.internal.ITempGenerator

internal class TempGenerator: ITempGenerator {
    override fun generateTempVariable(
        tempCounter: Int
    ): Pair<String, Int> {
        val address = "_t$tempCounter"
        return Pair(address, tempCounter + 1)
    }
}