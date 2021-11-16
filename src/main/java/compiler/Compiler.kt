package compiler

import compiler.core.TranslatedProgramRootNode
import compiler.parser.IParser
import compiler.tokenizer.ITokenizer
import compiler.translator.ITranslator

class Compiler(
    private val tokenizer: ITokenizer,
    private val parser: IParser,
    private val translator: ITranslator
) {
    fun compile(program: String): TranslatedProgramRootNode {
        val tokens = tokenizer.tokenize(program)
        val parseTree = parser.parse(tokens)
        val translatedTree = translator.translate(parseTree)

        return translatedTree
    }
}