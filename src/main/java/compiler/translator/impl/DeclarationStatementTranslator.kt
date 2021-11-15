package compiler.translator.impl

import compiler.core.IParsedDeclarationStatementNode
import compiler.core.ITranslatedDeclarationStatementNode
import compiler.core.ParsedFunctionDeclarationNode
import compiler.core.ParsedVariableDeclarationListNode
import compiler.translator.impl.internal.IDeclarationStatementTranslator
import compiler.translator.impl.internal.IFunctionDeclarationTranslator
import compiler.translator.impl.internal.IVariableDeclarationListTranslator

internal class DeclarationStatementTranslator(
    private val functionDeclarationTranslator: IFunctionDeclarationTranslator,
    private val variableDeclarationListTranslator: IVariableDeclarationListTranslator
): IDeclarationStatementTranslator {
    override fun translate(declarationStatementNode: IParsedDeclarationStatementNode): ITranslatedDeclarationStatementNode {
        return if (declarationStatementNode is ParsedVariableDeclarationListNode) {
            variableDeclarationListTranslator.translate(declarationStatementNode)
        } else {
            functionDeclarationTranslator.translate(declarationStatementNode as ParsedFunctionDeclarationNode)
        }
    }
}