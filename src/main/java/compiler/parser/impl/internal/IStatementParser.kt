package compiler.parser.impl.internal

import compiler.core.IStatementNode
import compiler.core.Token

internal interface IStatementParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IStatementNode, Int>
}