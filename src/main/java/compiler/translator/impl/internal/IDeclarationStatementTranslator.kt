package compiler.translator.impl.internal

import compiler.core.nodes.parsed.IParsedDeclarationStatementNode
import compiler.core.nodes.translated.ITranslatedDeclarationStatementNode

internal interface IDeclarationStatementTranslator {
    fun translate(
        declarationStatementNode: IParsedDeclarationStatementNode,
        variableToTypeMap: MutableMap<String, String>
    ): ITranslatedDeclarationStatementNode
}