package compiler.translator.impl

import compiler.core.*
import compiler.translator.impl.internal.IDeclarationStatementTranslator
import compiler.translator.impl.internal.IFunctionDeclarationTranslator

internal class DeclarationStatementTranslator(
    private val functionDeclarationTranslator: IFunctionDeclarationTranslator,
): IDeclarationStatementTranslator {
    override fun translate(declarationStatementNode: IParsedDeclarationStatementNode): ITranslatedDeclarationStatementNode {
        return if (declarationStatementNode is VariableDeclarationListNode) {
            declarationStatementNode
        } else {
            functionDeclarationTranslator.translate(declarationStatementNode as ParsedFunctionDeclarationNode)
        }
    }
}