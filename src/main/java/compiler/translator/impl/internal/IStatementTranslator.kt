package compiler.translator.impl.internal

import compiler.core.IParsedStatementNode
import compiler.core.ITranslatedStatementNode

internal interface IStatementTranslator {
    fun translate(statementNode: IParsedStatementNode): ITranslatedStatementNode
}