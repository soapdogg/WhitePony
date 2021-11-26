package compiler.translator

import compiler.core.nodes.parsed.ParsedProgramRootNode
import compiler.core.nodes.translated.TranslatedProgramRootNode

interface ITranslator {
    fun translate(rootNode: ParsedProgramRootNode): TranslatedProgramRootNode
}