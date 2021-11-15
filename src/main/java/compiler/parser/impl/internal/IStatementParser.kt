package compiler.parser.impl.internal

import compiler.core.IParsedStatementNode
import compiler.core.Token

internal interface IStatementParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedStatementNode, Int>
}