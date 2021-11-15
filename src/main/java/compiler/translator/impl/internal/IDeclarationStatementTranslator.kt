package compiler.translator.impl.internal

import compiler.core.IParsedDeclarationStatementNode
import compiler.core.ITranslatedDeclarationStatementNode

internal interface IDeclarationStatementTranslator {
    fun translate(declarationStatementNode: IParsedDeclarationStatementNode): ITranslatedDeclarationStatementNode
}