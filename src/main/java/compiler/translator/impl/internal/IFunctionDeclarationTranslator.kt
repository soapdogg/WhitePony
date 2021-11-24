package compiler.translator.impl.internal

import compiler.core.ParsedFunctionDeclarationNode
import compiler.core.TranslatedFunctionDeclarationNode

internal interface IFunctionDeclarationTranslator {
    fun translate(
        functionDeclarationNode: ParsedFunctionDeclarationNode,
        variableToTypeMap: MutableMap<String, String>
    ): TranslatedFunctionDeclarationNode
}