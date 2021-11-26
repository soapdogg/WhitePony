package compiler.translator.impl.internal

import compiler.core.nodes.parsed.ParsedReturnNode
import compiler.core.nodes.translated.TranslatedReturnNode

internal interface IReturnStatementTranslator {
    fun translate(
        returnNode: ParsedReturnNode,
        variableToTypeMap: Map<String, String>,
        tempCounter: Int
    ): Pair<TranslatedReturnNode, Int>
}