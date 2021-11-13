package compiler.parser.impl.internal

import compiler.core.ReturnNode
import compiler.core.Token

internal interface IReturnStatementParser {
    fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<ReturnNode, Int>
}