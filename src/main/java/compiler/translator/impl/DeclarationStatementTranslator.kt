package compiler.translator.impl

import compiler.core.*
import compiler.translator.impl.internal.IDeclarationStatementTranslator
import compiler.translator.impl.internal.IFunctionDeclarationTranslator
import compiler.translator.impl.internal.IVariableTypeRecorder

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