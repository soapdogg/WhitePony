package compiler.parser.impl.internal

import compiler.core.ParsedAssignNode
import compiler.core.Token

internal interface IAssignParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<ParsedAssignNode, Int>
}