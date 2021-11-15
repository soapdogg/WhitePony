package compiler.translator.impl

import compiler.core.ParsedProgramRootNode
import compiler.core.TranslatedProgramRootNode
import compiler.translator.ITranslator
import compiler.translator.impl.internal.IDeclarationStatementTranslator

internal class Translator(
    private val declarationStatementTranslator: IDeclarationStatementTranslator
): ITranslator {
    override fun translate(rootNode: ParsedProgramRootNode): TranslatedProgramRootNode {
        val translatedDeclarationStatements = rootNode.declarationStatements.map {
            declarationStatementTranslator.translate(it)
        }
        return TranslatedProgramRootNode(translatedDeclarationStatements)
    }
}