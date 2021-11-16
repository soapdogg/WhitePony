package compiler.parser.impl.internal

import compiler.core.ParsedReturnNode
import compiler.core.Token

internal interface IReturnStatementParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<ParsedReturnNode, Int>
}