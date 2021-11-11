package compiler.parser

import compiler.core.ProgramRootNode
import compiler.core.Token

interface IParser {
    fun parse(tokens: List<Token>): ProgramRootNode
}