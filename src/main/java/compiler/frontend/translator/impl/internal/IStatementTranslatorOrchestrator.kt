package compiler.frontend.translator.impl.internal

import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.translated.TranslatedBasicBlockNode

internal interface IStatementTranslatorOrchestrator {
    fun translate(
        statementNode: IParsedStatementNode,
        variableToTypeMap: MutableMap<String, String>
    ): TranslatedBasicBlockNode
}