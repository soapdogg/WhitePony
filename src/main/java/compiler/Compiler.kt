package compiler

import compiler.parser.IParser
import compiler.printer.IPrinter
import compiler.tokenizer.ITokenizer
import compiler.translator.ITranslator

class Compiler(
    private val tokenizer: ITokenizer,
    private val parser: IParser,
    private val translator: ITranslator,
    private val printer: IPrinter
) {
    fun compile(program: String): Pair<String, String> {
        val tokens = tokenizer.tokenize(program)
        val parseTree = parser.parse(tokens)
        val parseTreeString = printer.printNode(parseTree, true)
        val translatedTree = translator.translate(parseTree)
        val translatedTreeString = printer.printNode(translatedTree, false)

        return Pair(parseTreeString, translatedTreeString)
    }
}