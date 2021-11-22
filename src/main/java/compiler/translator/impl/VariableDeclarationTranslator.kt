package compiler.translator.impl

import compiler.core.ParsedVariableDeclarationNode
import compiler.core.TranslatedVariableDeclarationNode
import compiler.translator.impl.internal.IArrayTranslator
import compiler.translator.impl.internal.IAssignTranslator
import compiler.translator.impl.internal.IVariableDeclarationTranslator

internal class VariableDeclarationTranslator(
    private val arrayTranslator: IArrayTranslator,
    private val assignTranslator: IAssignTranslator
): IVariableDeclarationTranslator {
    override fun translate(
        variableDeclarationNode: ParsedVariableDeclarationNode,
        labelCounter: Int,
        tempCounter: Int
    ): TranslatedVariableDeclarationNode {

        val translatedArrayNode = if (variableDeclarationNode.arrayNode != null) {
            arrayTranslator.translate(
                variableDeclarationNode.arrayNode,
                labelCounter,
                tempCounter
            )
        } else {
            null
        }
        val translatedAssignNode = if (variableDeclarationNode.assignNode != null) {
            assignTranslator.translate(
                variableDeclarationNode.assignNode,
                labelCounter,
                tempCounter
            )
        } else {
            null
        }
        return TranslatedVariableDeclarationNode(
            variableDeclarationNode.id,
            translatedArrayNode,
            translatedAssignNode
        )
    }
}