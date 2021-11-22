package compiler.translator.impl.internal

import compiler.core.ParsedAssignNode
import compiler.core.TranslatedAssignNode

internal interface IAssignTranslator {
    fun translate(
        assignNode: ParsedAssignNode,
        labelCounter: Int,
        tempCounter: Int
    ): TranslatedAssignNode
}