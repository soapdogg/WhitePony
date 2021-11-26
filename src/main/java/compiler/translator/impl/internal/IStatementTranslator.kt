package compiler.translator.impl.internal

import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.translated.TranslatedBasicBlockNode

internal interface IStatementTranslator {
    fun translate(
        statementNode: IParsedStatementNode,
        variableToTypeMap: MutableMap<String, String>
    ): TranslatedBasicBlockNode
}