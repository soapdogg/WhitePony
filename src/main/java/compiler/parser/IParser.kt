package compiler.parser

import compiler.core.ParsedProgramRootNode
import compiler.core.Token

interface IParser {
    fun parse(tokens: List<Token>): ParsedProgramRootNode
}