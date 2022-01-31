package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.ParsedProgramRootNode
import compiler.core.nodes.translated.TranslatedProgramRootNode
import compiler.frontend.translator.ITranslator
import compiler.frontend.translator.impl.internal.IDeclarationStatementTranslator

internal class Translator(
    private val declarationStatementTranslator: IDeclarationStatementTranslator
): ITranslator {
    override fun translate(rootNode: ParsedProgramRootNode): TranslatedProgramRootNode {
        val variableToTypeMap = mutableMapOf<String, String>()
        val translatedDeclarationStatements = rootNode.declarationStatements.map {
            declarationStatementTranslator.translate(it, variableToTypeMap)
        }
        return TranslatedProgramRootNode(translatedDeclarationStatements)
    }
}