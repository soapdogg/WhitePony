package compiler.translator.impl

import compiler.core.ParsedVariableDeclarationListNode
import compiler.core.TranslatedVariableDeclarationListNode
import compiler.translator.impl.internal.IVariableDeclarationListTranslator
import compiler.translator.impl.internal.IVariableDeclarationTranslator

internal class VariableDeclarationListTranslator(
    private val variableDeclarationTranslator: IVariableDeclarationTranslator
):IVariableDeclarationListTranslator {
    override fun translate(
        variableDeclarationListNode: ParsedVariableDeclarationListNode
    ): TranslatedVariableDeclarationListNode {
        val translatedVariableDeclarations = variableDeclarationListNode.variableDeclarations.map {
            variableDeclarationTranslator.translate(it)
        }
        return TranslatedVariableDeclarationListNode(
            variableDeclarationListNode.type,
            translatedVariableDeclarations
        )
    }
}