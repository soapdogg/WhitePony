package compiler.parser.impl.internal

import compiler.core.ParsedBasicBlockNode
import compiler.core.Token

internal interface IStatementParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<ParsedBasicBlockNode, Int>
}