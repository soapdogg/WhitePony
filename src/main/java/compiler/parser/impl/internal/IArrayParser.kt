package compiler.parser.impl.internal

import compiler.core.ParsedArrayNode
import compiler.core.Token

internal interface IArrayParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<ParsedArrayNode, Int>
}