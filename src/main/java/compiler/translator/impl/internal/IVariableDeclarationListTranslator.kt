package compiler.translator.impl.internal

import compiler.core.ParsedVariableDeclarationListNode
import compiler.core.TranslatedVariableDeclarationListNode

internal interface IVariableDeclarationListTranslator {
    fun translate(
        variableDeclarationListNode: ParsedVariableDeclarationListNode,
        labelCounter: Int,
        tempCounter: Int,
    ): TranslatedVariableDeclarationListNode
}