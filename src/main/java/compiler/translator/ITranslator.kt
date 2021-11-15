package compiler.translator

import compiler.core.ParsedProgramRootNode
import compiler.core.TranslatedProgramRootNode

interface ITranslator {
    fun translate(rootNode: ParsedProgramRootNode): TranslatedProgramRootNode
}