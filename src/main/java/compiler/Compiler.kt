package compiler

import compiler.core.ParsedProgramRootNode
import compiler.parser.IParser
import compiler.tokenizer.ITokenizer

class Compiler(
    private val tokenizer: ITokenizer,
    private val parser: IParser
) {
    fun compile(program: String): ParsedProgramRootNode {
        val tokens = tokenizer.tokenize(program)
        val parseTree = parser.parse(tokens)

        return parseTree
    }
}