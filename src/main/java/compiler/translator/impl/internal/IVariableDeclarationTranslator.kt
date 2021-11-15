package compiler.translator.impl.internal

import compiler.core.ParsedVariableDeclarationNode
import compiler.core.TranslatedVariableDeclarationNode

internal interface IVariableDeclarationTranslator {
    fun translate(variableDeclarationNode: ParsedVariableDeclarationNode): TranslatedVariableDeclarationNode
}