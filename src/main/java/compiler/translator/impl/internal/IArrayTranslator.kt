package compiler.translator.impl.internal

import compiler.core.ParsedArrayNode
import compiler.core.TranslatedArrayNode

internal interface IArrayTranslator {
    fun translate(
        arrayNode: ParsedArrayNode,
        labelCounter: Int,
        tempCounter: Int
    ): TranslatedArrayNode
}