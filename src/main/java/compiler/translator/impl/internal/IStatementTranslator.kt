package compiler.translator.impl.internal

import compiler.core.IParsedStatementNode
import compiler.core.ITranslatedStatementNode
import compiler.core.TranslatedBasicBlockNode

internal interface IStatementTranslator {
    fun translate(
        statementNode: IParsedStatementNode,
        variableToTypeMap: MutableMap<String, String>
    ): TranslatedBasicBlockNode
}