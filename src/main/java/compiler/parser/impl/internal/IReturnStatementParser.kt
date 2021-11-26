package compiler.parser.impl.internal

import compiler.core.nodes.parsed.ParsedReturnNode
import compiler.core.tokenizer.Token

internal interface IReturnStatementParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<ParsedReturnNode, Int>
}