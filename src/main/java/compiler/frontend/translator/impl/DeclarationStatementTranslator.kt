package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.IParsedDeclarationStatementNode
import compiler.core.nodes.translated.ITranslatedDeclarationStatementNode
import compiler.core.nodes.parsed.ParsedFunctionDeclarationNode
import compiler.core.nodes.VariableDeclarationListNode
import compiler.frontend.translator.impl.internal.IDeclarationStatementTranslator
import compiler.frontend.translator.impl.internal.IFunctionDeclarationTranslator
import compiler.frontend.translator.impl.internal.IVariableTypeRecorder

internal class DeclarationStatementTranslator(
    private val functionDeclarationTranslator: IFunctionDeclarationTranslator,
    private val variableTypeRecorder: IVariableTypeRecorder
): IDeclarationStatementTranslator {
    override fun translate(
        declarationStatementNode: IParsedDeclarationStatementNode,
        variableToTypeMap: MutableMap<String, String>
    ): ITranslatedDeclarationStatementNode {
        return if (declarationStatementNode is VariableDeclarationListNode) {
            variableTypeRecorder.recordVariableTypes(declarationStatementNode, variableToTypeMap)
            declarationStatementNode
        } else {
            functionDeclarationTranslator.translate(declarationStatementNode as ParsedFunctionDeclarationNode, variableToTypeMap)
        }
    }
}