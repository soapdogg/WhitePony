package compiler.parser

import compiler.core.nodes.parsed.ParsedProgramRootNode
import compiler.core.tokenizer.Token

interface IParser {
    fun parse(
        tokens: List<Token>
    ): ParsedProgramRootNode
}