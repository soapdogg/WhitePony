package compiler.translator.impl.internal

import compiler.core.nodes.parsed.ParsedFunctionDeclarationNode
import compiler.core.nodes.translated.TranslatedFunctionDeclarationNode

internal interface IFunctionDeclarationTranslator {
    fun translate(
        functionDeclarationNode: ParsedFunctionDeclarationNode,
        variableToTypeMap: MutableMap<String, String>
    ): TranslatedFunctionDeclarationNode
}