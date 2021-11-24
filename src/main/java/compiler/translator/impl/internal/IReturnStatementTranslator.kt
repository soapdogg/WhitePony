package compiler.translator.impl.internal

import compiler.core.ParsedReturnNode
import compiler.core.TranslatedReturnNode

internal interface IReturnStatementTranslator {
    fun translate(
        returnNode: ParsedReturnNode,
        tempCounter: Int
    ): Pair<TranslatedReturnNode, Int>
}